package br.edu.ifba.inf012.medHealthAPI.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequestDto(
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    String email
) {
}
