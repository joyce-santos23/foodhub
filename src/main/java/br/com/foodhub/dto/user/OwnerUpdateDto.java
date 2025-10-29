package br.com.foodhub.dto.user;

import jakarta.validation.constraints.Pattern;
import io.swagger.v3.oas.annotations.media.Schema;

public record OwnerUpdateDto(
        @Schema(description = "Nome completo do proprietário", example = "Carlos Pereira")
        String name,

        @Schema(description = "Nome da empresa", example = "Restaurante Saboroso LTDA")
        String businessName,

        @Schema(description = "E-mail do proprietário", example = "carlos.pereira@email.com")
        String email,

        @Pattern(regexp = "\\d{11}", message = "Telefone deve ter 11 dígitos")
        @Schema(description = "Telefone do proprietário com DDD", example = "11987654321")
        String phone,

        @Schema(description = "CNPJ da empresa", example = "12345678000199")
        String cnpj
) {}
