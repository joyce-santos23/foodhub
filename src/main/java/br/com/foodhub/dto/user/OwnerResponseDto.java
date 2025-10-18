package br.com.foodhub.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record OwnerResponseDto(
        Long id,
        String name,
        String email,
        String phone,
        String cnpj,
        String businessName,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
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
