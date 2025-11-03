package br.com.foodhub.application.dto.address;

public record AddressBaseDto(
        String cep,
        String street,
        String neighborhood,
        String city,
        String state,
        String country
) {
    public String cep() {
        return String.format("%s-%s",
                cep.substring(0, 5),
                cep.substring(5)
        );
    }
}

