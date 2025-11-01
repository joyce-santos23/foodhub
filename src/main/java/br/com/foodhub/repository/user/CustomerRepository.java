package br.com.foodhub.repository.user;

import br.com.foodhub.entities.user.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends UserRepository<Customer> {
    Optional<Customer> findByCpf(String cpf);

    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
