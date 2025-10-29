package br.com.foodhub.dto.auth;

import br.com.foodhub.entities.user.UserRole;

public record LoginResponseDto(
        String accessToken,
        Long userId,
        String email,
        UserRole role
) {
}
