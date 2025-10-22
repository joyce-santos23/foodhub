package br.com.foodhub.service.user;

import br.com.foodhub.entities.user.Owner;
import br.com.foodhub.entities.user.Customer;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.exception.ResourceNotFoundException;
import br.com.foodhub.repository.user.CustomerRepository;
import br.com.foodhub.repository.user.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLookupService {

    private final OwnerRepository ownerRepository;
    private  final CustomerRepository customerRepository;

    public User findUserById(Long userId) {
        Owner owner = ownerRepository.findById(userId).orElse(null);
        if (owner != null) return owner;

        Customer customer = customerRepository.findById(userId).orElse(null);
        if (customer != null) return customer;

        throw new IllegalArgumentException("Usuário não encontrado");
    }

}
