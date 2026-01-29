package br.edu.ifba.inf012.medHealthAPI.controllers;

import br.edu.ifba.inf012.medHealthAPI.dtos.user.*;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Role;
import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import br.edu.ifba.inf012.medHealthAPI.services.JWTTokenService;
import br.edu.ifba.inf012.medHealthAPI.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationContoller {
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JWTTokenService tokenService;

  @Autowired
  private UserService userService;

  @PostMapping("/login")
  public ResponseEntity<JWTTokenData> login(@RequestBody AuthenticationDto dto){
      var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
      Authentication auth = this.authenticationManager.authenticate(usernamePassword);

      User user = (User) auth.getPrincipal();
      var token = this.tokenService.generateToken(user);

      return ResponseEntity.ok(new JWTTokenData(token));
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
