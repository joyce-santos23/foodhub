package br.com.foodhub.controller.address;

import br.com.foodhub.service.address.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.foodhub.dto.address.UserAddressRequestDto;
import br.com.foodhub.dto.address.UserAddressResponseDto;
import br.com.foodhub.dto.generic.ApiResponse;
import br.com.foodhub.entities.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/{userId}/address")
public class UserAddressController {
    private final UserAddressService service;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserAddressResponseDto>> getAll(
            @PathVariable Long userId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(service.getAll(user, userId));

    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAddressResponseDto> createAddress(
            @PathVariable Long userId,
            @RequestBody @Valid UserAddressRequestDto dto,
            @AuthenticationPrincipal User user
    ) {
        UserAddressResponseDto responseDto = service.create(
                userId,
                user,
                dto.cep(),
                dto.numberStreet(),
                dto.complement(),
                dto.primaryAddress() != null && dto.primaryAddress()

        );
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PutMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAddressResponseDto> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @RequestBody UserAddressRequestDto dto,
            @AuthenticationPrincipal User user
    ) {

        UserAddressResponseDto response = service.update(userId, addressId, dto, user);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @AuthenticationPrincipal User user
    ) {
        service.delete(userId, addressId, user);
        return ResponseEntity.ok(new ApiResponse("Endereço deletado com sucesso!"));

    }
}
