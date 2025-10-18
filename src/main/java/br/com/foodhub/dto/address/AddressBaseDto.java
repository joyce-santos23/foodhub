package br.com.foodhub.dto.address;

public record AddressBaseDto(
        String cep,
        String street,
        String neighborhood,
        String city,
        String state,
        String country
) {}

