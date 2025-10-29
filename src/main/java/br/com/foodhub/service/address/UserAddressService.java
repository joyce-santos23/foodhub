package br.com.foodhub.service.address;


import br.com.foodhub.dto.address.UserAddressRequestDto;
import br.com.foodhub.dto.address.UserAddressResponseDto;
import br.com.foodhub.entities.address.AddressBase;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.entities.address.UserAddress;
import br.com.foodhub.entities.user.UserRole;
import br.com.foodhub.exception.ResourceNotFoundException;
import br.com.foodhub.exception.ResourceOwnershipException;
import br.com.foodhub.mapper.address.UserAddressMapper;
import br.com.foodhub.repository.user.BaseUserRepository;
import br.com.foodhub.repository.user.UserAddressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        checkAuthorization(targetUserId, authenticatedUser);

        User targetUser = targetUserId.equals(authenticatedUser.getId())
                ? authenticatedUser
                : findTargetUser(targetUserId);

        AddressBase addressBase = addressBaseService.findOrCreateByCep(cep);

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
}
