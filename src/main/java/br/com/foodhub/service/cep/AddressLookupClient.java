package br.com.foodhub.service.cep;

import br.com.foodhub.dto.address.AddressBaseDto;

public interface AddressLookupClient {
    AddressBaseDto getAddressByCep(String cep);
}
