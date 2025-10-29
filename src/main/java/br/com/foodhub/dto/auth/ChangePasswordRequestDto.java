package br.com.foodhub.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(
        @NotBlank(message = "A senha atual é obrigatória.")
        String currentPassword,

        @NotBlank(message = "A nova senha é obrigatória.")
        @Size(min = 6, message = "A nova senha deve ter no mínimo 8 caracteres.")
        String newPassword
) {}
