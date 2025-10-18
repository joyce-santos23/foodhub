package br.com.foodhub.service.cep;

import br.com.foodhub.dto.address.AddressBaseDto;
import br.com.foodhub.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CepApiClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "https://viacep.com.br/ws/{cep}/json/";

    public AddressBaseDto getAddressByCep(String cep) {
        Map<String, String> response = restTemplate.getForObject(URL, Map.class, cep);

        if (response == null || response.get("erro") != null) {
            throw new ResourceNotFoundException("CEP não encontrado");
        }

        return new AddressBaseDto(
                response.get("cep"),
                response.get("logradouro"),
                response.get("bairro"),
                response.get("localidade"),
                response.get("uf"),
                "Brasil"
        );
    }
}
