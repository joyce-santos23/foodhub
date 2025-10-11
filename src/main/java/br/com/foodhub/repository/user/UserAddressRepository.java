package br.com.foodhub.repository.user;

import br.com.foodhub.entities.user.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
}
