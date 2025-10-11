package br.com.foodhub.repository.address;

import br.com.foodhub.entities.address.AddressBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<AddressBase, Long> {
}
