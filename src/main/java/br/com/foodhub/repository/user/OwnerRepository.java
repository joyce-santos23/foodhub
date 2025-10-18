package br.com.foodhub.repository.user;

import br.com.foodhub.entities.user.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Object> findByEmail(String email);

    Optional<Object> findByPhone(String phone);

    Optional<Object> findByCnpj(String cnpj);
}
