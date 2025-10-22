package br.com.foodhub.dto.address;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UserAddressResponseDto(
        Long id,
        AddressBaseDto address,
        String numberStreet,
        String complement,
        boolean primaryAddress,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
                LocalDateTime createdAt,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
        LocalDateTime updatedAt
) {
}
