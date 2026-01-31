package br.edu.ifba.inf012.medHealthAPI.controllers;

import br.edu.ifba.inf012.medHealthAPI.dtos.user.UserDto;
import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(UserDto.fromEntity(user));
  }
}
