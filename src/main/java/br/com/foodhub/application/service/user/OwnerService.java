package br.com.foodhub.application.service.user;

import br.com.foodhub.application.dto.pagination.PageResponseDto;
import br.com.foodhub.application.dto.user.OwnerRequestDto;
import br.com.foodhub.application.dto.user.OwnerResponseDto;
import br.com.foodhub.application.dto.user.OwnerUpdateDto;
import br.com.foodhub.domain.entities.user.Owner;
import br.com.foodhub.domain.entities.user.User;
import br.com.foodhub.domain.entities.user.UserRole;
import br.com.foodhub.domain.exception.MustReauthenticateException;
import br.com.foodhub.domain.exception.ResourceConflictException;
import br.com.foodhub.domain.exception.ResourceNotFoundException;
import br.com.foodhub.domain.mapper.user.OwnerMapper;
import br.com.foodhub.infrastructure.repository.user.OwnerRepository;
import br.com.foodhub.application.service.pagination.PaginationService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OwnerService {
    private OwnerRepository repository;
    private OwnerMapper mapper;
    private PasswordEncoder passwordEncoder;

    public PageResponseDto<OwnerResponseDto> findAllPaginated(int page, int size, String sortBy, boolean asc){
        return PaginationService.paginate(repository, page - 1, size, sortBy, asc, mapper::toResponse);
    }

    public PageResponseDto<OwnerResponseDto> findByNamePaginated(
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
        String encriptedPassword = passwordEncoder.encode(dto.password());
        owner.setPassword(encriptedPassword);
        owner.setRole(UserRole.OWNER);
        Owner saved = repository.save(owner);
        return mapper.toResponse(saved);
    }

    public void delete(Long id, User user) {
        Owner owner = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner com o ID " + id + " não encontrado!"));

        checkPermissionOrPossession(owner, user);
        repository.delete(owner);
    }

    public OwnerResponseDto update(Long id, OwnerUpdateDto dto, User user) {
        Owner owner = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não foi encontrado!"));

        checkPermissionOrPossession(owner, user);

        if (dto.name() != null && !dto.name().isBlank()) owner.setName(dto.name());
        if (dto.businessName() != null && !dto.businessName().isBlank()) owner.setBusinessName(dto.businessName());

        boolean emailChanged = false;
        if (dto.email() != null && !dto.email().isBlank() && !owner.getEmail().equals(dto.email())) {
            checkValidEmail(dto.email());
            owner.setEmail(dto.email());
            emailChanged = true;
        }

        if (dto.phone() != null && !owner.getPhone().equals(dto.phone())) {
            checkUniquePhone(dto.phone());
            owner.setPhone(dto.phone());
        }

        if (dto.cnpj() != null) {
            if (owner.getCnpj() == null) {
                checkUniqueCnpj(dto.cnpj());
                owner.setCnpj(dto.cnpj());
            } else if (!dto.cnpj().equals(owner.getCnpj())) {
                throw new ResourceConflictException("CNPJ não pode ser alterado uma vez cadastrado!");
            }
        }


        Owner updated = repository.save(owner);
        if (emailChanged) {
            throw new MustReauthenticateException("Seu e-mail foi alterado. Por favor, faça login novamente para obter um novo token.");
        }
        return mapper.toResponse(updated);
    }

    private void checkPermissionOrPossession(Owner owner, User authenticatedUser) {
        boolean isAdmin = authenticatedUser.getRole().equals(UserRole.ADMIN);
        boolean isResourceOwner = owner.getId().equals(authenticatedUser.getId());

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

    private void checkUniqueCnpj(String cnpj) {
        repository.findByCnpj(cnpj).ifPresent(c ->
        { throw new ResourceConflictException("Já existe um usuário com o CNPJ " + cnpj); });
    }

    private String normalizePhone(String phone) {
        if (phone == null) return null;
        return phone.replaceAll("\\D", "");
    }

}
