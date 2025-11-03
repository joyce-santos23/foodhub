package br.com.foodhub.presentation.controller.api.auth;

import br.com.foodhub.application.dto.auth.ChangePasswordRequestDto;
import br.com.foodhub.application.dto.auth.LoginRequestDto;
import br.com.foodhub.application.dto.auth.LoginResponseDto;
import br.com.foodhub.application.dto.auth.PasswordResetAdminDto;
import br.com.foodhub.application.dto.generic.ApiResponseGen;
import br.com.foodhub.domain.entities.user.User; // 🚨 IMPORT NECESSÁRIO
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

import javax.naming.AuthenticationException; // Manter este import se for usado, mas o Spring usa outras exceptions.
// Se você está usando javax.naming.AuthenticationException na sua AuthController,
// Mantenha-o. Se for a exceção de segurança do Spring, você pode remover.

/**
 * Interface de contrato para a AuthController, contendo todas as anotações
 * OpenAPI (Swagger) para documentação da API.
 */
@Tag(name = "Autenticação & Segurança", description = "Endpoints para login, obtenção de token e gerenciamento de senha.")
@RequestMapping("/api/v1/auth")
public interface AuthApi {

    // =================================================================
    // LOGIN (PÚBLICO)
    // =================================================================
    @Operation(
            summary = "Login de Usuário",
            description = "Autentica o usuário por e-mail ou telefone e retorna um Token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida, token gerado."),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (e-mail/telefone ou senha incorretos).",
                    content = @Content)
    })
    @PostMapping("/login")
    ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto);


    // =================================================================
    // ALTERAÇÃO PRÓPRIA DE SENHA (AUTENTICADO)
    // =================================================================
    @Operation(
            summary = "Alteração de Senha Própria",
            description = "Permite que o usuário autenticado altere sua própria senha, validando a senha atual.",
            security = @SecurityRequirement(name = "BearerAuth") // Exige o Token JWT
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Token ausente, inválido ou expirado.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Senha atual incorreta ou acesso negado.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Campos inválidos na requisição.",
                    content = @Content)
    })
    @PutMapping("/change-password")
    ResponseEntity<ApiResponseGen> changePassword(
            @RequestBody ChangePasswordRequestDto dto,
            @Parameter(hidden = true)
            User user
    ) throws AuthenticationException;

    // =================================================================
    // RESET DE SENHA POR ADMIN (ADMIN)
    // =================================================================
    @Operation(
            summary = "Resetar Senha de Terceiro (ADMIN)",
            description = "Permite que um administrador defina uma nova senha para qualquer usuário pelo ID.",
            security = @SecurityRequirement(name = "BearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Senha resetada com sucesso."),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido.",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Permissão negada. Requer papel ADMIN.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário alvo não encontrado.",
                    content = @Content)
    })
    @PutMapping("/{id}/password-reset")
    ResponseEntity<ApiResponseGen> resetPasswordByAdmin(
            @PathVariable("id")
            @Parameter(description = "ID do usuário (Owner ou Customer) que terá a senha resetada.")
            Long id,
            @RequestBody @Valid PasswordResetAdminDto dto
    );
}
