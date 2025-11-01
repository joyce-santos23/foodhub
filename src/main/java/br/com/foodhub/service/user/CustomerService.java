package br.com.foodhub.service.user;

import br.com.foodhub.dto.pagination.PageResponseDto;
import br.com.foodhub.dto.user.CustomerRequestDto;
import br.com.foodhub.dto.user.CustomerResponseDto;
import br.com.foodhub.dto.user.CustomerUpdateDto;
import br.com.foodhub.entities.user.Customer;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.entities.user.UserRole;
import br.com.foodhub.exception.MustReauthenticateException;
import br.com.foodhub.exception.ResourceConflictException;
import br.com.foodhub.exception.ResourceNotFoundException;
import br.com.foodhub.mapper.user.CustomerMapper;
import br.com.foodhub.repository.user.CustomerRepository;
import br.com.foodhub.service.pagination.PaginationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerService {
    private CustomerRepository repository;
    private CustomerMapper mapper;
    private PasswordEncoder passwordEncoder;

    public PageResponseDto<CustomerResponseDto> findAllPaginated(int page, int size, String sortBy, boolean asc){
        return PaginationService.paginate(repository, page - 1, size, sortBy, asc, mapper::toResponse);
    }

    public PageResponseDto<CustomerResponseDto> findByNamePaginated(
            String name, int page, int size, String sortBy, boolean asc
    ) {
        return PaginationService.paginateWithSearch(
                pageable -> repository.findByNameContainingIgnoreCase(name, pageable),
                page - 1,
                size,
                sortBy,
                asc,
                mapper::toResponse
        );
    }

    public CustomerResponseDto findById(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceConflictException("Usuário com ID " + id + " não foi encontrado!"));
        return mapper.toResponse(customer);
    }
    @Transactional
    public CustomerResponseDto save(CustomerRequestDto dto) {
        checkUniqueEmail(dto.email());
        checkUniquePhone(normalizePhone(dto.phone()));
        if(dto.cpf() != null) {
            checkUniqueCpf(dto.cpf());
        }

        Customer customer = mapper.toEntity(dto);
        String encriptedPassword = passwordEncoder.encode(dto.password());
        customer.setPassword(encriptedPassword);
        customer.setRole(UserRole.CUSTOMER);
        Customer saved = repository.save(customer);
        return mapper.toResponse(saved);
    }

    public void delete(Long id, User user) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com o ID " + id + " não encontrado!"));

        checkPermissionOrPossession(customer, user);
        repository.delete(customer);
    }

    public CustomerResponseDto update(Long id, CustomerUpdateDto dto, User user) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não foi encontrado!"));

        checkPermissionOrPossession(customer, user);
        if (dto.name() != null && !dto.name().isBlank()) customer.setName(dto.name());

        boolean emailChanged = false;
        if (dto.email() != null && !dto.email().isBlank() && !customer.getEmail().equals(dto.email())) {
            checkValidEmail(dto.email());
            customer.setEmail(dto.email());
            emailChanged = true;
        }

        if (dto.phone() != null && !customer.getPhone().equals(dto.phone())) {
            checkUniquePhone(dto.phone());
            customer.setPhone(dto.phone());
        }

        if (dto.cpf() != null) {
            if (customer.getCpf() == null) {
                checkUniqueCpf(dto.cpf());
                customer.setCpf(dto.cpf());
            } else if (!dto.cpf().equals(customer.getCpf())) {
                throw new ResourceConflictException("CPF não pode ser alterado uma vez cadastrado!");
            }
        }

        Customer updated = repository.save(customer);
        if (emailChanged) {
            throw new MustReauthenticateException("Seu e-mail foi alterado. Por favor, faça login novamente para obter um novo token.");
        }
        return mapper.toResponse(updated);
    }

    private void checkPermissionOrPossession(Customer customer, User authenticatedUser) {
        boolean isAdmin = authenticatedUser.getRole().equals(UserRole.ADMIN);
        boolean isResourceOwner = customer.getId().equals(authenticatedUser.getId());

        if (!isAdmin && !isResourceOwner) {
            throw new IllegalArgumentException("Você não tem permissão para manipular este recurso.");
        }
    }

    private void checkValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(emailRegex)) {
            throw new ResourceConflictException("E-mail inválido");
        }
        checkUniqueEmail(email);
    }

    private void checkUniqueEmail(String email) {
        repository.findByEmail(email).ifPresent(c ->
        { throw new ResourceConflictException("Já existe um usuário com o e-mail " + email); });
    }

    private void checkUniquePhone(String phone) {
        repository.findByPhone(phone).ifPresent(c ->
        { throw new ResourceConflictException("Já existe um usuário com o telefone " + phone); });
    }

    private void checkUniqueCpf(String cpf) {
        repository.findByCpf(cpf).ifPresent(c ->
        { throw new ResourceConflictException("Já existe um usuário com o CPF " + cpf); });
    }

    private String normalizePhone(String phone) {
        if (phone == null) return null;
        return phone.replaceAll("\\D", "");
    }

}
