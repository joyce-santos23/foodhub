package br.com.foodhub.presentation.controller.address;

import br.com.foodhub.infrastructure.config.security.UserPrincipal;
import br.com.foodhub.presentation.controller.api.address.UserAddressApi;
import br.com.foodhub.application.service.address.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.foodhub.application.dto.address.UserAddressRequestDto;
import br.com.foodhub.application.dto.address.UserAddressResponseDto;
import br.com.foodhub.application.dto.generic.ApiResponseGen;
import br.com.foodhub.domain.entities.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/{userId}/address")
public class UserAddressController implements UserAddressApi {
    private final UserAddressService service;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserAddressResponseDto>> getAllAddress(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserPrincipal principal) {
        User user = principal.getUser();
        return ResponseEntity.ok(service.getAll(user, userId));

    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserAddressResponseDto> createAddress(
            @PathVariable Long userId,
            @RequestBody @Valid UserAddressRequestDto dto,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = principal.getUser();
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
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = principal.getUser();
        UserAddressResponseDto response = service.update(userId, addressId, dto, user);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseGen> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @AuthenticationPrincipal UserPrincipal principal
    ) {
        User user = principal.getUser();
        service.delete(userId, addressId, user);
        return ResponseEntity.ok(new ApiResponseGen("Endereço deletado com sucesso!"));

    }
}
