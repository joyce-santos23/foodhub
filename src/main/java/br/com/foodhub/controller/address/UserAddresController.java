package br.com.foodhub.controller.address;

import br.com.foodhub.dto.address.UserAddressRequestDto;
import br.com.foodhub.dto.address.UserAddressResponseDto;
import br.com.foodhub.dto.generic.ApiResponse;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.service.address.UserAddressService;
import br.com.foodhub.service.user.UserLookupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/address")
public class UserAddresController {

    private final UserAddressService service;
    private final UserLookupService userLookupService;

    @GetMapping
    public ResponseEntity<List<UserAddressResponseDto>> getAll(@PathVariable Long userId) {
        User user = userLookupService.findUserById(userId);
        return ResponseEntity.ok(service.getAll(user));
    }

    @PostMapping
    public ResponseEntity<UserAddressResponseDto> createAddress(
            @PathVariable Long userId,
            @RequestBody @Valid UserAddressRequestDto dto
            ) {
        User user = userLookupService.findUserById(userId);
        UserAddressResponseDto responseDto = service.create(
                user,
                dto.cep(),
                dto.numberStreet(),
                dto.complement(),
                dto.primaryAddress() != null && dto.primaryAddress()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<UserAddressResponseDto> updateAddres(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @RequestBody UserAddressRequestDto dto
    ) {
        User user = userLookupService.findUserById(userId);
        UserAddressResponseDto response = service.update(addressId, dto, user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId
    ) {
        User user = userLookupService.findUserById(userId);
        service.delete(addressId);
        return ResponseEntity.ok(new ApiResponse("Endereço deletado com sucesso!"));
    }
}
