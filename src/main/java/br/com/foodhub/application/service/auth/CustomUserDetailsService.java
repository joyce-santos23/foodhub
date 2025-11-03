package br.com.foodhub.application.service.auth;

import br.com.foodhub.domain.entities.user.User;
import br.com.foodhub.infrastructure.repository.user.CustomerRepository;
import br.com.foodhub.infrastructure.repository.user.OwnerRepository;
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
