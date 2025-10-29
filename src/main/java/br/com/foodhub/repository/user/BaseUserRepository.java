package br.com.foodhub.repository.user;

import br.com.foodhub.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseUserRepository extends JpaRepository<User, Long> {
}
