package br.com.foodhub.entities.address;

import br.com.foodhub.entities.address.AddressBase;
import br.com.foodhub.entities.base.BaseEntity;
import br.com.foodhub.entities.user.User;
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressBase address;
    private String complement;
    private String numberStreet;
    @Column(name = "primary_address", nullable = true)
    private boolean primaryAddress;
}
