package br.com.foodhub.application.dto.auth;

import br.com.foodhub.domain.entities.user.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto(
        @Schema(description = "Token JWT de autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,

        @Schema(description = "ID do usuário", example = "1")
        Long userId,

        @Schema(description = "E-mail do usuário", example = "usuario@email.com")
        String email,

        @Schema(description = "Papel do usuário", example = "ADMIN")
        UserRole role
) {
}
