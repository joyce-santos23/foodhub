package br.com.foodhub.service.user;

import br.com.foodhub.dto.pagination.PageResponseDto;
import br.com.foodhub.dto.user.CustomerRequestDto;
import br.com.foodhub.dto.user.CustomerResponseDto;
import br.com.foodhub.entities.user.Customer;
import br.com.foodhub.exception.ResourceConflictException;
import br.com.foodhub.exception.ResourceNotFoundException;
import br.com.foodhub.mapper.user.CustomerMapper;
import br.com.foodhub.repository.user.CustomerRepository;
import br.com.foodhub.service.pagination.PaginationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CustomerService {
    private CustomerRepository repository;
    private CustomerMapper mapper;

    public PageResponseDto<CustomerResponseDto> findAllPaginated(int page, int size, String sortBy, boolean asc){
        return PaginationService.paginate(repository, page - 1, size, sortBy, asc, mapper::toResponse);
    }

    public CustomerResponseDto findById(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceConflictException("Usuário com ID " + id + " não foi encontrado!"));
        return mapper.toResponse(customer);
    }

    public CustomerResponseDto save(CustomerRequestDto dto) {
        checkUniqueEmail(dto.email());
        checkUniquePhone(dto.phone());
        if(dto.cpf() != null) {
            checkUniqueCpf(dto.cpf());
        }

        Customer customer = mapper.toEntity(dto);
        Customer saved = repository.save(customer);
        return mapper.toResponse(saved);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public CustomerResponseDto update(Long id, CustomerRequestDto dto) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não foi encontrado!"));

        if (!customer.getEmail().equals(dto.email())) {
            checkUniqueEmail(dto.email());
            customer.setEmail(dto.email());
        }

        if (!customer.getPhone().equals(dto.phone())) {
            checkUniquePhone(dto.phone());
            customer.setPhone(dto.phone());
        }

        if (dto.cpf() != null && !dto.cpf().equals(customer.getCpf())) {
            checkUniqueCpf(dto.cpf());
            customer.setCpf(dto.cpf());
        }

        customer.setName(dto.name());

        Customer updated = repository.save(customer);
        return mapper.toResponse(updated);
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
}
