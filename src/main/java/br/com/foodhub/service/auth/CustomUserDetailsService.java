package br.com.foodhub.service.auth;

import br.com.foodhub.entities.user.Owner;
import br.com.foodhub.entities.user.Customer;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.repository.user.BaseUserRepository;
import br.com.foodhub.repository.user.CustomerRepository;
import br.com.foodhub.repository.user.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;
    private  final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {

        Optional<User> userOptional = ownerRepository.findByEmail(identifier)
                .map(user -> (User) user);
        userOptional = userOptional.or(() -> customerRepository.findByEmail(identifier).map(user -> (User) user));
        userOptional = userOptional.or(() -> ownerRepository.findByPhone(identifier).map(user -> (User) user));
        userOptional = userOptional.or(() -> customerRepository.findByPhone(identifier).map(user -> (User) user));


        return userOptional.orElseThrow(() ->
                new UsernameNotFoundException("Credencial " + identifier + " não encontrada.")
        );
    }
}
