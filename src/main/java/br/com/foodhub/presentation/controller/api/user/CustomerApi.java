package br.com.foodhub.presentation.controller.api.user;

import br.com.foodhub.application.dto.generic.ApiResponseGen;
import br.com.foodhub.application.dto.pagination.PageResponseDto;
import br.com.foodhub.application.dto.user.CustomerRequestDto;
import br.com.foodhub.application.dto.user.CustomerResponseDto;
import br.com.foodhub.application.dto.user.CustomerUpdateDto;
import br.com.foodhub.infrastructure.config.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Interface de contrato para a CustomerController.
 * Contém a documentação OpenAPI e o mapeamento de endpoints.
 */
@Tag(name = "Clientes", description = "Operações de consulta e registro de clientes (Customers).")
@RequestMapping("/api/v1/customers")
public interface CustomerApi {

    // =================================================================
    // GET ALL (ADMIN)
    // ... (MÉTODO findAll inalterado) ...
    @Operation(
            summary = "Listar Clientes (ADMIN)",
            description = "Retorna uma lista paginada de todos os clientes no sistema.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = CustomerResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Permissão negada. Requer papel ADMIN.",
                    content = @Content)
    })
    @GetMapping
    ResponseEntity<PageResponseDto<CustomerResponseDto>> findAll(
            @Parameter(description = "Número da página (inicia em 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(defaultValue = "asc") String direction
    );

    // =================================================================
    // GET BY ID (ADMIN)
    // ... (MÉTODO findById inalterado) ...
    @Operation(
            summary = "Buscar Cliente por ID (ADMIN)",
            description = "Retorna detalhes de um cliente específico pelo ID.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = CustomerResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Permissão negada. Requer papel ADMIN.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<CustomerResponseDto> findByid(@PathVariable Long id);

    // =================================================================
    // GET BY NAME (ADMIN) - NOVO MÉTODO
    // =================================================================
    @Operation(
            summary = "Buscar Clientes por Nome (ADMIN)",
            description = "Retorna uma lista paginada de clientes cujo nome contenha o termo de busca.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = PageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Permissão negada. Requer papel ADMIN.",
                    content = @Content)
    })
    @GetMapping("/search")
    ResponseEntity<PageResponseDto<CustomerResponseDto>> findByName(
            @Parameter(description = "Nome ou parte do nome a ser buscado.")
            @RequestParam String name,
            @Parameter(description = "Número da página (inicia em 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(defaultValue = "asc") String direction
    );

    // =================================================================
    // GET ME (AUTENTICADO)
    // =================================================================
    @Operation(
            summary = "Buscar Perfil Próprio",
            description = "Retorna os dados do cliente autenticado via token.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
                    content = @Content),
    })
    @GetMapping("/me")
    ResponseEntity<CustomerResponseDto> getAuthenticatedCustomer(
            @Parameter(hidden = true) UserPrincipal principal
    );

    // =================================================================
    // CREATE (PÚBLICO)
    // ... (MÉTODO create inalterado) ...
    @Operation(
            summary = "Registro de Novo Cliente",
            description = "Cria uma nova conta de cliente no sistema. Rota pública."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso."),
            @ApiResponse(responseCode = "409", description = "Dados de entrada inválidos ou CPF/Email/Telefone já em uso.",
                    content = @Content)
    })
    @PostMapping
    ResponseEntity<CustomerResponseDto> create(@Valid @RequestBody CustomerRequestDto dto);

    // =================================================================
    // UPDATE (AUTENTICADO - CHECAGEM DE POSSE)
    // =================================================================
    @Operation(
            summary = "Atualizar Perfil",
            description = "Atualiza os dados de um cliente. Requer que o usuário seja o dono do ID ou Admin.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. O recurso não pertence ao usuário.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Dados de entrada inválidos ou CPF/Email/Telefone já em uso.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<CustomerResponseDto> update(
            @PathVariable Long id,
            @RequestBody CustomerUpdateDto dto,
            @Parameter(hidden = true)UserPrincipal principal
            );

    // =================================================================
    // DELETE (AUTENTICADO - CHECAGEM DE POSSE)
    // =================================================================
    @Operation(
            summary = "Deletar Cliente",
            description = "Deleta a conta de um cliente pelo ID. Requer que o usuário seja o dono do ID ou Admin.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente deletado com sucesso."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. O recurso não pertence ao usuário.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponseGen> delete(
            @PathVariable Long id,
            @Parameter(hidden = true) UserPrincipal principal
    );
}
