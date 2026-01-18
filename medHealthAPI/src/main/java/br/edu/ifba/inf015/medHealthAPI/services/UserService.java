package br.edu.ifba.inf015.medHealthAPI.services;

import br.edu.ifba.inf015.medHealthAPI.dtos.user.UserDto;
import br.edu.ifba.inf015.medHealthAPI.dtos.user.UserFormDto;
import br.edu.ifba.inf015.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf015.medHealthAPI.models.entities.Role;
import br.edu.ifba.inf015.medHealthAPI.models.entities.User;
import br.edu.ifba.inf015.medHealthAPI.repositories.RoleRepository;
import br.edu.ifba.inf015.medHealthAPI.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public UserDto register(UserFormDto userForm){
        User user = new User(userForm);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role defaultRole = roleRepository.findByRole("ROLE_USER")
                .orElseThrow(() -> new EntityNotFoundException("Default role not found"));
        user.setRole(defaultRole);
        return UserDto.fromEntity(userRepository.save(user));
    }
}
