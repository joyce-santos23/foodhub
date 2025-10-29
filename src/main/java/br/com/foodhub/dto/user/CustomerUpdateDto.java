package br.com.foodhub.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerUpdateDto(
        @Schema(description = "Nome completo do cliente", example = "Maria Silva")
        String name,

        @Email(message = "E-mail inválido")
        @Schema(description = "E-mail do cliente", example = "maria.silva@email.com")
        String email,

        @Pattern(regexp = "\\d{11}", message = "Telefone deve ter 11 dígitos")
        @Schema(description = "Telefone do cliente com DDD", example = "11987654321")
        String phone,

        @Schema(description = "CPF do cliente", example = "12345678901")
        String cpf
) {}
