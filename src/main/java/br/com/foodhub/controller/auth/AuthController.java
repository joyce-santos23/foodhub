package br.com.foodhub.controller.auth;

import br.com.foodhub.controller.api.auth.AuthApi;
import br.com.foodhub.controller.api.user.OwnerApi;
import br.com.foodhub.dto.auth.ChangePasswordRequestDto;
import br.com.foodhub.dto.auth.LoginRequestDto;
import br.com.foodhub.dto.auth.LoginResponseDto;
import br.com.foodhub.dto.auth.PasswordResetAdminDto;
import br.com.foodhub.dto.generic.ApiResponseGen;
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
public class AuthController implements AuthApi {

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
    public ResponseEntity<ApiResponseGen> changePassword(
            @RequestBody ChangePasswordRequestDto dto,
            @AuthenticationPrincipal User user
            ) throws AuthenticationException {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        userSecurityService.changePassword(user, dto.currentPassword(), dto.newPassword());
        return ResponseEntity.ok(new ApiResponseGen("Senha alterada com sucesso!"));
    }

    @PutMapping("{id}/password-reset")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponseGen> resetPasswordByAdmin(
            @PathVariable Long id,
            @RequestBody @Valid PasswordResetAdminDto dto
            ) {
        userSecurityService.resetPassword(id, dto.newPassword());
        return ResponseEntity.ok(new ApiResponseGen("Senha alterada com sucesso!"));
    }

}
