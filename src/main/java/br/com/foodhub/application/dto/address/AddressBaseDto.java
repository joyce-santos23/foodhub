package br.com.foodhub.application.dto.address;

public record AddressBaseDto(
        String cep,
        String street,
        String neighborhood,
        String city,
        String state,
        String country
) {}

