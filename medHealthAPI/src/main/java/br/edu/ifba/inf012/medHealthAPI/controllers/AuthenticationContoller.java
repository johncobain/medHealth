package br.edu.ifba.inf012.medHealthAPI.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest.DoctorRequestFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.patient.PatientDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.patient.PatientFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.AuthenticationDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.ChangePasswordDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.LoginResponseDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.ResetPasswordDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.ResetPasswordRequestDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.ProfileInfoDto;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Role;
import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import br.edu.ifba.inf012.medHealthAPI.services.AuthenticationService;
import br.edu.ifba.inf012.medHealthAPI.services.PatientService;
import br.edu.ifba.inf012.medHealthAPI.services.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationContoller {

  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private UserService userService;

  @Autowired
  private PatientService patientService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody AuthenticationDto dto){
      LoginResponseDto response = authenticationService.login(dto);
      return ResponseEntity.ok(response);
  }

  @PostMapping("/register")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<PatientDto> create(@Valid @RequestBody PatientFormDto dto) {
    PatientDto patient = patientService.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(patient);
  }

  @PostMapping("/request-register")
  public ResponseEntity<Void> register(@RequestBody @Valid DoctorRequestFormDto dto){
    userService.requestRegister(dto);
    return ResponseEntity.status(201).build();
  }

  @GetMapping("/profile")
  public ResponseEntity<ProfileInfoDto> getProfile(@AuthenticationPrincipal User user) {
    ProfileInfoDto profile = new ProfileInfoDto(
      user.getId(),
      user.getPerson().getId(),
      user.getUsername(),
      user.getPerson().getFullName(),
      user.getPerson().getPhone(),
      user.getPerson().getCpf(),
      user.getRoles().stream().map(Role::getAuthority).findFirst().orElse("")
    );
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
