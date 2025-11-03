package br.com.foodhub.infrastructure.repository.user;

import br.com.foodhub.domain.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseUserRepository extends JpaRepository<User, Long> {
}
