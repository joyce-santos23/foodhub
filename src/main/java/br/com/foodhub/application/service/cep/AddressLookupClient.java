package br.com.foodhub.application.service.cep;

import br.com.foodhub.application.dto.address.AddressBaseDto;

public interface AddressLookupClient {
    AddressBaseDto getAddressByCep(String cep);
}
