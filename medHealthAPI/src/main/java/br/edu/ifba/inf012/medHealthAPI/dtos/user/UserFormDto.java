package br.edu.ifba.inf012.medHealthAPI.dtos.user;

import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserFormDto (
        @NotBlank(message = "ID de Pessoa é obrigatório")
        Long personId,
        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        @Size(max = 72, message = "Senha deve ter no máximo 72 caracteres")
        String password
){
   public UserFormDto(User user) {
       this(user.getPerson().getId(), user.getPassword());
   }
}
