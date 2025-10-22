package br.com.foodhub.service.address;


import br.com.foodhub.dto.address.UserAddressRequestDto;
import br.com.foodhub.dto.address.UserAddressResponseDto;
import br.com.foodhub.entities.address.AddressBase;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.entities.address.UserAddress;
import br.com.foodhub.exception.ResourceNotFoundException;
import br.com.foodhub.mapper.address.UserAddressMapper;
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
    private final UserAddressMapper mapper;

    public List<UserAddressResponseDto> getAll(User user) {
        List<UserAddress> addresses = repository.findByUser(user);

        return addresses.stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Transactional
    public UserAddressResponseDto create(User user, String cep, String number, String complement, boolean prymaryAddress) {
        AddressBase addressBase = addressBaseService.findOrCreateByCep(cep);

        if (prymaryAddress) {
            repository.unsetPrimaryAddresses(user);
        }

        UserAddress userAddress = UserAddress.builder()
                .user(user)
                .address(addressBase)
                .numberStreet(number)
                .complement(complement)
                .primaryAddress(prymaryAddress)
                .build();

        repository.save(userAddress);
        return  mapper.toResponseDto(userAddress);
    }

    @Transactional
    public UserAddressResponseDto update(Long addresId, UserAddressRequestDto dto, User user) {
        UserAddress userAddress = repository.findById(addresId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço com o ID " + addresId + " não encontrado!"));

        if (!userAddress.getUser().getId().equals(user.getId())) {
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

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Endereço com ID " + id + " não encontrado!");
        }
        repository.deleteById(id);
    }
}
