package br.com.foodhub.application.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UserAddressRequestDto(
        @NotBlank(message = "CEP não pode ser nulo")
        String cep,

        @NotBlank(message = "Número da rua não pode ser nulo")
        String numberStreet,

        String complement,
        Boolean primaryAddress
) {
}
