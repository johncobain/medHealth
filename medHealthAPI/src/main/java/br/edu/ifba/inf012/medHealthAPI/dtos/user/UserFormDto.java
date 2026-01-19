package br.edu.ifba.inf012.medHealthAPI.dtos.user;

import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserFormDto (
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = "Password is required")
        @Size(max = 72, message = "Password must be at most 72 characters long")
        String password
){
   public UserFormDto(User user) {
       this(user.getUsername(), user.getEmail(), user.getPassword());
   }
}
