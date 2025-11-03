package br.com.foodhub.application.dto.user;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CustomerResponseDto(
        @Schema(description = "ID do cliente", example = "1")
        Long id,

        @Schema(description = "Nome completo do cliente", example = "João da Silva")
        String name,

        @Schema(description = "E-mail do cliente", example = "joao.silva@email.com")
        String email,

        @Schema(description = "Telefone formatado do cliente", example = "(11) 98765-4321")
        String phone,

        @Schema(description = "CPF do cliente", example = "123.456.789-01")
        String cpf,

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

    public String cpf() {
        if (cpf == null) return null;
        String digits = cpf.replaceAll("\\D", "");

        if (digits.length() != 11) {
            return cpf;
        }

        return String.format("%s.%s.%s-%s",
                digits.substring(0, 3),
                digits.substring(3, 6),
                digits.substring(6, 9),
                digits.substring(9)
        );
    }
}
