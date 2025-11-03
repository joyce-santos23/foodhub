package br.com.foodhub.infrastructure.config.commandLineIssues;

import br.com.foodhub.domain.entities.user.Customer;
import br.com.foodhub.domain.entities.user.Owner;
import br.com.foodhub.domain.entities.user.UserRole;
import br.com.foodhub.infrastructure.repository.user.CustomerRepository;
import br.com.foodhub.infrastructure.repository.user.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final CustomerRepository customerRepository;
    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initializeDatabase() {
        return args -> {

            String defaultPassword = "defaultPassword123";
            String encryptedPassword = passwordEncoder.encode(defaultPassword);

            // --- 1. CRIAÇÃO DO ADMIN (USER ROLE ADMIN) ---
            createAdmin(encryptedPassword);

            // --- 2. CRIAÇÃO DE USUÁRIOS DE TESTE (CUSTOMER) ---
            createTestCustomers(encryptedPassword);

            // --- 3. CRIAÇÃO DE USUÁRIOS DE TESTE (OWNER) ---
            createTestOwners(encryptedPassword);
        };
    }

    private void createAdmin(String encryptedPassword) {
        if (ownerRepository.findByEmail("admin@foodhub.com").isEmpty()) {
            Owner admin = Owner.builder()
                    .name("Administrador FoodHub")
                    .email("admin@foodhub.com")
                    .password(encryptedPassword)
                    .phone("99999999999")
                    .businessName("FoodHub HQ - Administrativo")
                    .role(UserRole.ADMIN)
                    .build();
            ownerRepository.save(admin);
            System.out.println(">>> Usuário Admin inicial criado.");
        }
    }

    private void createTestCustomers(String encryptedPassword) {
        if (customerRepository.findByEmail("customer.test@foodhub.com").isEmpty()) {
            Customer customer1 = Customer.builder()
                    .name("Cliente Teste 1")
                    .email("customer.test@foodhub.com")
                    .password(encryptedPassword)
                    .phone("11987654321")
                    .role(UserRole.CUSTOMER)
                    .build();
            customerRepository.save(customer1);

            System.out.println(">>> Usuários Customer de teste criados.");
        }
    }

    private void createTestOwners(String encryptedPassword) {
        if (ownerRepository.findByEmail("owner.test@foodhub.com").isEmpty()) {
            Owner owner1 = Owner.builder()
                    .name("Dono Restaurante A")
                    .email("owner.test@foodhub.com")
                    .password(encryptedPassword)
                    .phone("21912345678")
                    .businessName("Restaurante de Teste LTDA")
                    .role(UserRole.OWNER)
                    .build();
            ownerRepository.save(owner1);

            System.out.println(">>> Usuários Owner de teste criados.");
        }
    }
}