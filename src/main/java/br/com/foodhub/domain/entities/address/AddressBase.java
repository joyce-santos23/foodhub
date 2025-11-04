package br.com.foodhub.domain.entities.address;

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
    @Column(length = 8, nullable = false)
    private String cep;

    @Column(length = 150, nullable = false)
    private String street;

    @Column(length = 100, nullable = false)
    private String neighborhood;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 50, nullable = false)
    private String state;

    @Column(length = 50, nullable = false)
    private String country;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private List<UserAddress> userAddress;
}
