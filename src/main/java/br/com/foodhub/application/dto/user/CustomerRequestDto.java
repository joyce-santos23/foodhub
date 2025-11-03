package br.com.foodhub.application.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequestDto(
        @NotBlank(message = "O nome não pode ser nulo")
        @Schema(description = "Nome completo do cliente", example = "João da Silva")
        String name,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "O e-mail não pode ser nulo")
        @Schema(description = "E-mail do cliente", example = "joao.silva@email.com")
        String email,

        @NotBlank(message = "O telefone não pode ser nulo")
        @Schema(description = "Telefone do cliente com DDD", example = "11987654321")
        String phone,

        @Schema(description = "CPF do cliente", example = "12345678901")
        String cpf,

        @NotBlank(message = "A senha não pode ser nula")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        @Schema(description = "Senha do cliente", example = "senha123")
        String password
) {}
