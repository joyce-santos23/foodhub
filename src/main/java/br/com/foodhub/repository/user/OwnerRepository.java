package br.com.foodhub.repository.user;

import br.com.foodhub.entities.user.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends UserRepository<Owner> {
    Optional<Owner> findByCnpj(String cnpj);
    Page<Owner> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
