package br.com.foodhub.application.service.address;


import br.com.foodhub.application.dto.address.UserAddressRequestDto;
import br.com.foodhub.application.dto.address.UserAddressResponseDto;
import br.com.foodhub.domain.entities.address.AddressBase;
import br.com.foodhub.domain.entities.user.User;
import br.com.foodhub.domain.entities.address.UserAddress;
import br.com.foodhub.domain.entities.user.UserRole;
import br.com.foodhub.domain.exception.InvalidDataException;
import br.com.foodhub.domain.exception.ResourceNotFoundException;
import br.com.foodhub.domain.exception.ResourceOwnershipException;
import br.com.foodhub.domain.mapper.address.UserAddressMapper;
import br.com.foodhub.infrastructure.repository.user.BaseUserRepository;
import br.com.foodhub.infrastructure.repository.user.UserAddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserAddressService {

    private final UserAddressRepository repository;
    private final AddressBaseService addressBaseService;
    private final BaseUserRepository baseUserRepository;
    private final UserAddressMapper mapper;

    public List<UserAddressResponseDto> getAll(User authenticatedUser, Long targetUserId) {
        checkAuthorization(targetUserId, authenticatedUser);

        User targetUser = targetUserId.equals(authenticatedUser.getId())
                ? authenticatedUser
                : findTargetUser(targetUserId);

        List<UserAddress> addresses = repository.findByUser(targetUser);

        return addresses.stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Transactional
    public UserAddressResponseDto create(
            Long targetUserId,
            User authenticatedUser,
            String cep,
            String number,
            String complement,
            boolean prymaryAddress) {
        String normalizedCep = normalizeCep(cep);
        checkAuthorization(targetUserId, authenticatedUser);

        User targetUser = targetUserId.equals(authenticatedUser.getId())
                ? authenticatedUser
                : findTargetUser(targetUserId);

        AddressBase addressBase = addressBaseService.findOrCreateByCep(normalizedCep);

        if (prymaryAddress) {
            repository.unsetPrimaryAddresses(targetUser);
        }

        UserAddress userAddress = UserAddress.builder()
                .user(targetUser)
                .address(addressBase)
                .numberStreet(number)
                .complement(complement)
                .primaryAddress(prymaryAddress)
                .build();

        repository.save(userAddress);
        return  mapper.toResponseDto(userAddress);
    }

    @Transactional
    public UserAddressResponseDto update(Long targetUserId, Long addresId, UserAddressRequestDto dto, User authenticatedUser) {
        checkAuthorization(targetUserId, authenticatedUser);

        UserAddress userAddress = repository.findById(addresId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço com o ID " + addresId + " não encontrado!"));

        if (!userAddress.getUser().getId().equals(targetUserId)) {
            throw new IllegalArgumentException("O endereço não pertence a este usuário!");
        }

        if (dto.numberStreet() != null && !dto.numberStreet().isBlank()) {
            userAddress.setNumberStreet(dto.numberStreet());
        }

        if (dto.complement() != null && !dto.complement().isBlank()) {
            userAddress.setComplement(dto.complement());
        }

        if (dto.primaryAddress() != null && dto.primaryAddress()) {
            repository.unsetPrimaryAddresses(userAddress.getUser(), userAddress.getId());
            userAddress.setPrimaryAddress(true);
        }
        repository.save(userAddress);
        return mapper.toResponseDto(userAddress);
    }

    public void delete(Long targetUserId, Long addressId, User user) {
        checkAuthorization(targetUserId, user);

        UserAddress address = repository.findById(addressId)
                        .orElseThrow(() -> new ResourceNotFoundException("Endereço com ID " + addressId + " não encontrado!"));
        if ((!address.getUser().getId().equals(targetUserId))){
            throw new IllegalArgumentException("O endereço não pertence a este usuário.");
        }
        repository.deleteById(addressId);
    }

    private void checkAuthorization(Long targetUserId, User authenticatedUser) {
        boolean isAdmin = authenticatedUser.getRole().equals(UserRole.ADMIN);
        boolean isSelf = targetUserId.equals(authenticatedUser.getId());

        if (!isAdmin && !isSelf) {
            throw new ResourceOwnershipException("Você não tem permissão para acessar ou modificar recursos de outros usuários.");
        }
    }

    private User findTargetUser(Long userId) {
        return baseUserRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário alvo com ID " + userId + " não encontrado!"));
    }

    private String normalizeCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("CEP não pode ser nulo ou vazio.");
        }

        String normalizedCep = cep.replaceAll("\\D", "");

        if (normalizedCep.length() != 8) {
            throw new InvalidDataException("CEP inválido. Deve conter 8 dígitos.");
        }

        return normalizedCep;
    }
}
