package br.com.foodhub.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotBlank(message = "O identificador (e-mail ou telefone) é obrigatório.")
        @Schema(description = "E-mail ou telefone do usuário", example = "usuario@email.com")
        String identifier,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        @Schema(description = "Senha do usuário", example = "senha123")
        String password
) {}
