package br.com.foodhub.entities.address;

import br.com.foodhub.entities.user.UserAddress;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "address_base")
public class AddressBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cep;
    private String street;
    private String Neighborhood;
    private String city;
    private String state;
    private String country;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private List<UserAddress> userAddress;
}
