package br.edu.ifba.inf012.medHealthAPI.controllers;

import br.edu.ifba.inf012.medHealthAPI.dtos.user.*;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Role;
import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import br.edu.ifba.inf012.medHealthAPI.services.AuthenticationService;
import br.edu.ifba.inf012.medHealthAPI.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationContoller {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody AuthenticationDto dto){
      LoginResponseDto response = authenticationService.login(dto);
      return ResponseEntity.ok(response);
  }

  @GetMapping("/profile")
  public ResponseEntity<Map<String, Object>> getProfile(@AuthenticationPrincipal User user) {
    Map<String, Object> profile = new HashMap<>();
    profile.put("id", user.getId());
    profile.put("personId", user.getPerson().getId());
    profile.put("email", user.getUsername());
    profile.put("fullName", user.getPerson().getFullName());
    profile.put("phone", user.getPerson().getPhone());
    profile.put("cpf", user.getPerson().getCpf());
    profile.put("roles", user.getRoles().stream().map(Role::getAuthority).toList());

    return ResponseEntity.ok(profile);
  }

  @PostMapping("/change-password")
  public ResponseEntity<Map<String, Object>> changePassword(
      @RequestBody @Valid ChangePasswordDto dto,
      @AuthenticationPrincipal User user
  ) {
    userService.changePassword(user.getId(), dto);
    return ResponseEntity.ok(Map.of("message", "Senha alterada com sucesso"));
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody @Valid ResetPasswordRequestDto dto) {
    userService.requestPasswordReset(dto);
    return ResponseEntity.ok(Map.of("message", "Email de recuperação enviado com sucesso"));
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody @Valid ResetPasswordDto dto) {
    userService.resetPassword(dto);
    return ResponseEntity.ok(Map.of("message", "Senha redefinida com sucesso"));
  }
}
