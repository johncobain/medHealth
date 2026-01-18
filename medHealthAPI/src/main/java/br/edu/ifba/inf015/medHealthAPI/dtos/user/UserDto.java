package br.edu.ifba.inf015.medHealthAPI.dtos.user;


import br.edu.ifba.inf015.medHealthAPI.models.entities.User;

public record UserDto(
        String username,
        String email
) {
    public UserDto(User user) {
        this(user.getUsername(), user.getEmail());
    }

    public static UserDto fromEntity(User user){
        return new UserDto(user);
    }
}
