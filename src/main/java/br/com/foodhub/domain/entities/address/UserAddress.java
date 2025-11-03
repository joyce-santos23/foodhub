package br.com.foodhub.domain.entities.address;

import br.com.foodhub.domain.entities.base.BaseEntity;
import br.com.foodhub.domain.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_address")
public class UserAddress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private AddressBase address;

    @Column(length = 100)
    private String complement;

    @Column(name = "number_street", length = 20, nullable = false)
    private String numberStreet;

    @Column(name = "primary_address", nullable = false)
    private boolean primaryAddress;
}
