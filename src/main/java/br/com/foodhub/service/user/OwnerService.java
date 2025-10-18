package br.com.foodhub.service.user;

import br.com.foodhub.dto.pagination.PageResponseDto;
import br.com.foodhub.dto.user.OwnerRequestDto;
import br.com.foodhub.dto.user.OwnerResponseDto;
import br.com.foodhub.entities.user.Owner;
import br.com.foodhub.exception.ResourceConflictException;
import br.com.foodhub.exception.ResourceNotFoundException;
import br.com.foodhub.mapper.user.OwnerMapper;
import br.com.foodhub.repository.user.OwnerRepository;
import br.com.foodhub.service.pagination.PaginationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OwnerService {
    private OwnerRepository repository;
    private OwnerMapper mapper;

    public PageResponseDto<OwnerResponseDto> findAllPaginated(int page, int size, String sortBy, boolean asc){
        return PaginationService.paginate(repository, page - 1, size, sortBy, asc, mapper::toResponse);
    }

    public OwnerResponseDto findById(Long id) {
        Owner owner = repository.findById(id)
                .orElseThrow(() -> new ResourceConflictException("Usuário com ID " + id + " não foi encontrado!"));
        return mapper.toResponse(owner);
    }

    public OwnerResponseDto save(OwnerRequestDto dto) {
        checkUniqueEmail(dto.email());
        checkUniquePhone(normalizePhone(dto.phone()));
        if(dto.cnpj() != null) {
            checkUniqueCnpj(dto.cnpj());
        }

        Owner owner = mapper.toEntity(dto);
        Owner saved = repository.save(owner);
        return mapper.toResponse(saved);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário com ID " + id + " não encontrado!");
        }
        repository.deleteById(id);
    }

    public OwnerResponseDto update(Long id, OwnerRequestDto dto) {
        Owner owner = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não foi encontrado!"));
        if (dto.name() != null && !dto.name().isBlank()) owner.setName(dto.name());

        if (dto.businessName() != null && !dto.businessName().isBlank()) owner.setBusinessName(dto.businessName());

        if (dto.email() != null && !dto.email().isBlank()) {
            checkValidEmail(dto.email());
            if (!owner.getEmail().equals(dto.email())) {
                owner.setEmail(dto.email());
            }
        }

        if (dto.phone() != null && !owner.getPhone().equals(dto.phone())) {
            checkUniquePhone(dto.phone());
            owner.setPhone(dto.phone());
        }

        if (dto.cnpj() != null && !dto.cnpj().equals(owner.getCnpj())) {
            checkUniqueCnpj(dto.cnpj());
            owner.setCnpj(dto.cnpj());
        }

        Owner updated = repository.save(owner);
        return mapper.toResponse(updated);
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

    private void checkUniqueCnpj(String cnpj) {
        repository.findByCnpj(cnpj).ifPresent(c ->
        { throw new ResourceConflictException("Já existe um usuário com o CNPJ " + cnpj); });
    }

    private String normalizePhone(String phone) {
        if (phone == null) return null;
        return phone.replaceAll("\\D", "");
    }

}
