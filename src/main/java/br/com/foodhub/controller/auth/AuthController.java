package br.com.foodhub.controller.auth;

import br.com.foodhub.dto.auth.ChangePasswordRequestDto;
import br.com.foodhub.dto.auth.LoginRequestDto;
import br.com.foodhub.dto.auth.LoginResponseDto;
import br.com.foodhub.dto.auth.PasswordResetAdminDto;
import br.com.foodhub.dto.generic.ApiResponse;
import br.com.foodhub.entities.user.User;
import br.com.foodhub.service.auth.TokenService;
import br.com.foodhub.service.auth.UserSecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserSecurityService userSecurityService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.identifier(), dto.password());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        User user = (User) auth.getPrincipal();
        String jwtToken = tokenService.generateToken(user);

        LoginResponseDto response = new LoginResponseDto(
                jwtToken,
                user.getId(),
                user.getEmail(),
                user.getRole());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @RequestBody ChangePasswordRequestDto dto,
            @AuthenticationPrincipal User user
            ) throws AuthenticationException {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        userSecurityService.changePassword(user, dto.currentPassword(), dto.newPassword());
        return ResponseEntity.ok(new ApiResponse("Senha alterada com sucesso!"));
    }

    @PutMapping("{id}/password-reset")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> resetPasswordByAdmin(
            @PathVariable Long id,
            @RequestBody @Valid PasswordResetAdminDto dto
            ) {
        userSecurityService.resetPassword(id, dto.newPassword());
        return ResponseEntity.ok(new ApiResponse("Senha alterada com sucesso!"));
    }

}
