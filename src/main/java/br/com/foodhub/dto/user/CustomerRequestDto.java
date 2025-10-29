package br.com.foodhub.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerRequestDto(
        @NotBlank(message = "O nome não pode ser nulo")
        String name,
        @Email(message = "E-mail inválido")
        @NotBlank(message = "O e-mail não pode ser nulo")
        String email,
        @Pattern(regexp = "\\d{11}", message = "Telefone deve ter 11 dígitos")
        @NotBlank(message = "O telefone não pode ser nulo")
        String phone,
        String cpf,
        @NotBlank(message = "A senha não pode ser nula")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
        String password
) {
}
