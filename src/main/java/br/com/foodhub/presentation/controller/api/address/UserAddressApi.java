package br.com.foodhub.presentation.controller.api.address;

import br.com.foodhub.application.dto.address.UserAddressRequestDto;
import br.com.foodhub.application.dto.address.UserAddressResponseDto;
import br.com.foodhub.application.dto.generic.ApiResponseGen;
import br.com.foodhub.domain.entities.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Interface de contrato para a UserAddressController.
 * Gerencia operações de endereço aninhadas ao ID do usuário.
 */
@Tag(name = "Endereços (Aninhado)", description = "Gerenciamento de endereços de usuários (Customer ou Owner).")
@RequestMapping("/api/v1/{userId}/address")
public interface UserAddressApi {

    // =================================================================
    // GET ALL (Consulta todos os endereços do usuário {userId})
    // =================================================================
    @Operation(
            summary = "Listar Endereços do Usuário Alvo",
            description = "Retorna todos os endereços do usuário especificado por {userId}. Requer que o usuário logado seja o próprio {userId} ou ADMIN.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Não autorizado (Token ausente/inválido).",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Tentativa de acessar recurso de outro usuário).",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário alvo não encontrado.",
                    content = @Content)
    })
    @GetMapping
    ResponseEntity<List<UserAddressResponseDto>> getAllAddress(
            @PathVariable("userId")
            @Parameter(description = "ID do usuário (dono) alvo dos endereços.")
            Long userId,
            @Parameter(hidden = true) User user
    );

    // =================================================================
    // POST (Criar novo endereço para o usuário {userId})
    // =================================================================
    @Operation(
            summary = "Criar Novo Endereço",
            description = "Cria um novo endereço e o associa ao usuário especificado por {userId}.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Endereço criado e associado."),
            @ApiResponse(responseCode = "400", description = "Dados de endereço inválidos.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Tentativa de criar endereço para outro usuário).",
                    content = @Content)
    })
    @PostMapping
    ResponseEntity<UserAddressResponseDto> createAddress(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid UserAddressRequestDto dto,
            @Parameter(hidden = true) User user // 🚨 CORRIGIDO: Tipo User
    );

    // =================================================================
    // PUT (Atualizar um endereço específico {addressId})
    // =================================================================
    @Operation(
            summary = "Atualizar Endereço Específico",
            description = "Atualiza um endereço específico ({addressId}) que pertence ao usuário {userId}.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Não é o dono ou Admin, ou o endereço não pertence ao {userId}).",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado.",
                    content = @Content)
    })
    @PutMapping("/{addressId}")
    ResponseEntity<UserAddressResponseDto> updateAddress(
            @PathVariable("userId") Long userId,
            @PathVariable("addressId") Long addressId,
            @RequestBody UserAddressRequestDto dto,
            @Parameter(hidden = true) User user
    );

    // =================================================================
    // DELETE
    // =================================================================
    @Operation(
            summary = "Deletar Endereço Específico",
            description = "Deleta um endereço específico ({addressId}).",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Endereço deletado com sucesso."),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Não é o dono, Admin, ou o endereço não pertence ao {userId}).",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Endereço não encontrado.",
                    content = @Content)
    })
    @DeleteMapping("/{addressId}")
    ResponseEntity<ApiResponseGen> deleteAddress(
            @PathVariable("userId") Long userId,
            @PathVariable("addressId") Long addressId,
            @Parameter(hidden = true) User user
    );
}
