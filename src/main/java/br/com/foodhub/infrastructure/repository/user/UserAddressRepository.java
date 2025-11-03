package br.com.foodhub.infrastructure.repository.user;

import br.com.foodhub.domain.entities.user.User;
import br.com.foodhub.domain.entities.address.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    @Modifying
    @Query("UPDATE UserAddress ua SET ua.primaryAddress = false WHERE ua.user = :user AND ua.primaryAddress = true")
    void unsetPrimaryAddresses(@Param("user") User user);

    @Modifying
    @Query("UPDATE UserAddress ua SET ua.primaryAddress = false WHERE ua.user = :user AND ua.id <> :currentId")
    void unsetPrimaryAddresses(@Param("user") User user, @Param("currentId") Long currentId);

    List<UserAddress> findByUser(User user);
}
