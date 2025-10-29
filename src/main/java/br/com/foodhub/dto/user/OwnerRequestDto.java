package br.com.foodhub.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OwnerRequestDto(
        @NotBlank(message = "O nome não pode ser nulo")
        @Schema(description = "Nome completo do proprietário", example = "Carlos Pereira")
        String name,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "O e-mail não pode ser nulo")
        @Schema(description = "E-mail do proprietário", example = "carlos.pereira@email.com")
        String email,

        @Pattern(regexp = "\\d{11}", message = "Telefone deve ter 11 dígitos")
        @NotBlank(message = "O telefone não pode ser nulo")
        @Schema(description = "Telefone do proprietário com DDD", example = "11987654321")
        String phone,

        @NotBlank(message = "O CNPJ não pode ser nulo")
        @Schema(description = "CNPJ da empresa do proprietário", example = "12.345.678/0001-90")
        String cnpj,

        @NotBlank(message = "O nome da empresa não pode ser nulo")
        @Schema(description = "Nome da empresa", example = "Restaurante Saboroso LTDA")
        String businessName,

        @NotBlank(message = "A senha não pode ser nula")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        @Schema(description = "Senha do proprietário", example = "senha123")
        String password
) {}
