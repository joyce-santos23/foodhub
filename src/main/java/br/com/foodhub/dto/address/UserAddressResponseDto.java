package br.com.foodhub.dto.address;

import jakarta.validation.constraints.NotBlank;
public record UserAddressResponseDto(
        Long id,
        AddressBaseDto address,
        String numberStreet,
        String complement,
        boolean primaryAddress
) {
}
