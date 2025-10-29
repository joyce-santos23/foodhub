package br.com.foodhub.service.auth;

import br.com.foodhub.entities.user.Customer;
import br.com.foodhub.entities.user.Owner;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.exception.ResourceNotFoundException;
import br.com.foodhub.repository.user.BaseUserRepository;
import br.com.foodhub.repository.user.CustomerRepository;
import br.com.foodhub.repository.user.OwnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class UserSecurityService {

    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final BaseUserRepository baseUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void changePassword(User user, String currentPassword, String newPassword) throws AuthenticationException {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new AuthenticationException("A senha atual fornecida está incorreta.");
        }

        String newEncryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newEncryptedPassword);

        if (user instanceof Customer customer) {
            customerRepository.save(customer);
        } else if (user instanceof Owner owner) {
            ownerRepository.save(owner);
        }
    }

    public void resetPassword(Long id, String newPassword) {
        User user = baseUserRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com o ID " + id + " não encontrado."));
        String newEncryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(newEncryptedPassword);

        if (user instanceof Customer customer) {
            customerRepository.save(customer);
        } else if (user instanceof Owner owner) {
            ownerRepository.save(owner);
        }
    }
}
