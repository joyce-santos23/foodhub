package br.com.foodhub.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
        @NotBlank(message = "A senha atual é obrigatória.")
        @Schema(description = "Senha atual do usuário", example = "senha123")
        String currentPassword,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 6, message = "A nova senha deve ter no mínimo 8 caracteres.")
        @Schema(description = "Nova senha do usuário", example = "novaSenha123")
        String newPassword
) {}
