package br.com.foodhub.repository.address;

import br.com.foodhub.entities.address.AddressBase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressBase, Long> {
    Optional<AddressBase> findByCepAndStreetAndCityAndState(String cep, String street, String city, String state);
}
