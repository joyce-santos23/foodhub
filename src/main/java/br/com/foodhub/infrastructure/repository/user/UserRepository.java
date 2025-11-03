package br.com.foodhub.infrastructure.repository.user;

import br.com.foodhub.domain.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface UserRepository<T extends User> extends JpaRepository< T, Long> {
    Optional<T> findByEmail(String email);
    Optional<T> findByPhone(String phone);
}
