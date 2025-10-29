package br.com.foodhub.controller.api.user;

import br.com.foodhub.dto.generic.ApiResponseGen;
import br.com.foodhub.dto.pagination.PageResponseDto;
import br.com.foodhub.dto.user.OwnerRequestDto;
import br.com.foodhub.dto.user.OwnerResponseDto;
import br.com.foodhub.dto.user.OwnerUpdateDto;
import br.com.foodhub.entities.user.User; // 🚨 IMPORT NECESSÁRIO
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
 * Interface de contrato para a OwnerController.
 * Contém a documentação OpenAPI e o mapeamento de endpoints para Owners.
 */
@Tag(name = "Proprietários", description = "Operações de consulta e registro de proprietários (Owners).")
@RequestMapping("/api/v1/owners")
public interface OwnerApi {

    // =================================================================
    // GET ALL (ADMIN)
    // =================================================================
    @Operation(
            summary = "Listar Proprietários (ADMIN)",
            description = "Retorna uma lista paginada de todos os proprietários no sistema.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = PageResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido."),
            @ApiResponse(responseCode = "403", description = "Permissão negada. Requer papel ADMIN.")
    })
    @GetMapping
    ResponseEntity<PageResponseDto<OwnerResponseDto>> findAll(
            @Parameter(description = "Número da página (inicia em 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Direção da ordenação (asc/desc)") @RequestParam(defaultValue = "asc") String direction
    );

    // =================================================================
    // GET BY ID (ADMIN)
    // =================================================================
    @Operation(
            summary = "Buscar Proprietário por ID (ADMIN)",
            description = "Retorna detalhes de um proprietário específico pelo ID.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proprietário encontrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = OwnerResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Permissão negada. Requer papel ADMIN.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Proprietário não encontrado.",
                    content = @Content)
    })
    @GetMapping("/{id}")
    ResponseEntity<OwnerResponseDto> findByid(@PathVariable Long id);

    // =================================================================
    // GET ME (AUTENTICADO)
    // =================================================================
    @Operation(
            summary = "Buscar Perfil Próprio",
            description = "Retorna os dados do proprietário autenticado via token.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado.")
    })
    @GetMapping("/me")
    ResponseEntity<OwnerResponseDto> getAuthenticatedOwner(
            @Parameter(hidden = true) User user
    );

    // =================================================================
    // CREATE (PÚBLICO)
    // =================================================================
    @Operation(
            summary = "Registro de Novo Proprietário",
            description = "Cria uma nova conta de proprietário no sistema. Rota pública."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proprietário criado com sucesso.",
                    content = @Content(schema = @Schema(implementation = OwnerResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos ou CNPJ/Email/Telefone já em uso.",
                    content = @Content)
    })
    @PostMapping
    ResponseEntity<OwnerResponseDto> create(@Valid @RequestBody OwnerRequestDto dto);

    // =================================================================
    // UPDATE (AUTENTICADO - CHECAGEM DE POSSE)
    // =================================================================
    @Operation(
            summary = "Atualizar Perfil",
            description = "Atualiza os dados de um proprietário. Requer que o usuário seja o dono do ID ou Admin.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização realizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = OwnerResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado. O recurso não pertence ao usuário.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Proprietário não encontrado.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    ResponseEntity<OwnerResponseDto> update(
            @PathVariable Long id,
            @RequestBody OwnerUpdateDto dto,
            @Parameter(hidden = true) User user
    );

    // =================================================================
    // DELETE (AUTENTICADO - CHECAGEM DE POSSE)
    // =================================================================
    @Operation(
            summary = "Deletar Proprietário",
            description = "Deleta a conta de um proprietário pelo ID. Requer que o usuário seja o dono do ID ou Admin.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proprietário deletado com sucesso.",
                    content = @Content(schema = @Schema(implementation = ApiResponseGen.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado. O recurso não pertence ao usuário.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Proprietário não encontrado.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponseGen> delete(
            @PathVariable Long id,
            @Parameter(hidden = true) User user
    );
}
