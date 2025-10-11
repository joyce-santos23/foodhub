package br.com.foodhub.repository.user;

import br.com.foodhub.entities.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
