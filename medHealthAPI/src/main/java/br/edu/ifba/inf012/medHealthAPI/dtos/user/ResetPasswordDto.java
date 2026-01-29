package br.edu.ifba.inf012.medHealthAPI.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordDto(
    @NotBlank(message = "Token é obrigatório")
    String token,

    @NotBlank(message = "Nova senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Size(max = 72, message = "Senha deve ter no máximo 72 caracteres")
    String newPassword
) {
}
