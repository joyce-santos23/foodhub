package br.com.foodhub.application.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserAddressRequestDto(
        @NotBlank(message = "CEP não pode ser nulo")
        @Pattern(regexp = "\\d{8}", message = "CEP deve ter 8 dígitos")
        String cep,

        @NotBlank(message = "Número da rua não pode ser nulo")
        String numberStreet,

        String complement,
        Boolean primaryAddress
) {
}
