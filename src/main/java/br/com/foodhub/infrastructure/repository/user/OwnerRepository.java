package br.com.foodhub.infrastructure.repository.user;

import br.com.foodhub.domain.entities.user.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OwnerRepository extends UserRepository<Owner> {
    Optional<Owner> findByCnpj(String cnpj);
    Page<Owner> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
