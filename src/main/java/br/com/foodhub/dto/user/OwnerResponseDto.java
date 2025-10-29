package br.com.foodhub.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record OwnerResponseDto(
        @Schema(description = "ID do proprietário", example = "1")
        Long id,

        @Schema(description = "Nome completo do proprietário", example = "Carlos Pereira")
        String name,

        @Schema(description = "E-mail do proprietário", example = "carlos.pereira@email.com")
        String email,

        @Schema(description = "Telefone formatado do proprietário", example = "(11) 98765-4321")
        String phone,

        @Schema(description = "CNPJ da empresa", example = "12.345.678/0001-90")
        String cnpj,

        @Schema(description = "Nome da empresa", example = "Restaurante Saboroso LTDA")
        String businessName,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Schema(description = "Data de criação do registro", example = "29/10/2025 16:30")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        @Schema(description = "Data da última atualização do registro", example = "29/10/2025 17:00")
        LocalDateTime updatedAt
) {
        public String phone() {
                if (phone == null || phone.length() != 11) return phone;
                return String.format("(%s) %s-%s",
                        phone.substring(0, 2),
                        phone.substring(2, 7),
                        phone.substring(7)
                );
        }
}
