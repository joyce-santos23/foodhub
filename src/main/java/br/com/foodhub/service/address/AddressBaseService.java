package br.com.foodhub.service.address;

import br.com.foodhub.dto.address.AddressBaseDto;
import br.com.foodhub.entities.address.AddressBase;
import br.com.foodhub.repository.address.AddressRepository;
import br.com.foodhub.service.cep.AddressLookupClient;
import br.com.foodhub.service.cep.CepApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AddressBaseService {
    private final AddressRepository addressRepository;
    private final AddressLookupClient cepApiClient;

    public AddressBase findOrCreateByCep(String cep) {
        return addressRepository.findByCep(cep)
                .orElseGet(() -> {
                    AddressBaseDto dto = cepApiClient.getAddressByCep(cep);
                    AddressBase address = new AddressBase();
                    address.setCep(dto.cep());
                    address.setStreet(dto.street());
                    address.setNeighborhood(dto.neighborhood());
                    address.setCity(dto.city());
                    address.setState(dto.state());
                    address.setCountry(dto.country());
                    return addressRepository.save(address);
                });
    }
}

