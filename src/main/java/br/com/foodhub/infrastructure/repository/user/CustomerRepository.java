package br.com.foodhub.infrastructure.repository.user;

import br.com.foodhub.domain.entities.user.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomerRepository extends UserRepository<Customer> {
    Optional<Customer> findByCpf(String cpf);

    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
