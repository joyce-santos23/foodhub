package br.com.foodhub.repository.user;

import br.com.foodhub.entities.user.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
