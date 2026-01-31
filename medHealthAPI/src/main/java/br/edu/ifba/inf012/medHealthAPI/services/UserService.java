package br.edu.ifba.inf012.medHealthAPI.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest.DoctorRequestFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.ChangePasswordDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.ResetPasswordDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.ResetPasswordRequestDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.UserDto;
import br.edu.ifba.inf012.medHealthAPI.exceptions.UniqueAttributeAlreadyRegisteredException;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf012.medHealthAPI.models.entities.DoctorRequest;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Person;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Role;
import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import br.edu.ifba.inf012.medHealthAPI.repositories.DoctorRequestRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.PersonRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.RoleRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final DoctorRequestRepository doctorRequestRepository;
  private final PersonRepository personRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%";
  private static final int TEMP_PASSWORD_LENGTH = 8;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmailService emailService, DoctorRequestRepository doctorRequestRepository, PersonRepository personRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.roleRepository = roleRepository;
    this.emailService = emailService;
    this.doctorRequestRepository = doctorRequestRepository;
    this.personRepository = personRepository;
  }

  @Transactional
  public UserDto createUserForPerson(Person person, boolean isDoctor) {
    if (userRepository.existsByPersonId(person.getId())) {
      throw new RuntimeException("Usuário já existe para esta pessoa");
    }

    String tempPassword = generateTempPassword();

    String roleName = isDoctor ? "ROLE_DOCTOR" : "ROLE_PATIENT";

    Role role = roleRepository.findByRole(roleName)
      .orElseThrow(() -> new RuntimeException("Role " + roleName + " não encontrada"));

    Set<Role> roles = new HashSet<>();
    roles.add(role);

    User user = new User(person, passwordEncoder.encode(tempPassword), roles);
    User savedUser = userRepository.save(user);

    emailService.sendCredentialsEmail(person, tempPassword, isDoctor);

    return UserDto.fromEntity(savedUser);
  }

  @Transactional
  public void changePassword(Long userId, ChangePasswordDto dto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    if (!passwordEncoder.matches(dto.oldPassword(), user.getPassword())) {
      throw new RuntimeException("Senha atual incorreta");
    }

    user.setPassword(passwordEncoder.encode(dto.newPassword()));
    userRepository.save(user);
  }

  @Transactional
  public void requestPasswordReset(ResetPasswordRequestDto dto) {
    User user = userRepository.findByPersonEmail(dto.email())
        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

    String token = UUID.randomUUID().toString();
    user.setPasswordResetToken(token);
    user.setPasswordResetExpires(LocalDateTime.now().plusHours(1));
    userRepository.save(user);

    emailService.sendPasswordResetEmail(user, token);
  }

  @Transactional
  public void resetPassword(ResetPasswordDto dto) {
    User user = userRepository.findByPasswordResetToken(dto.token())
        .orElseThrow(() -> new RuntimeException("Token inválido"));

    if (user.getPasswordResetExpires().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Token expirado");
    }

    user.setPassword(passwordEncoder.encode(dto.newPassword()));
    user.setPasswordResetToken(null);
    user.setPasswordResetExpires(null);
    userRepository.save(user);
  }

  public String generateTempPassword() {
    SecureRandom random = new SecureRandom();
    StringBuilder password = new StringBuilder(TEMP_PASSWORD_LENGTH);

    for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
      password.append(CHARS.charAt(random.nextInt(CHARS.length())));
    }

    return password.toString();
  }

  public void requestRegister(DoctorRequestFormDto dto) {
    if (personRepository.existsByEmail(dto.email())) {
      throw new UniqueAttributeAlreadyRegisteredException(Doctor.class.getSimpleName(), "email");
    }

    if (personRepository.existsByCpf(dto.cpf())) {
      throw new UniqueAttributeAlreadyRegisteredException(Doctor.class.getSimpleName(), "cpf");
    }

    if (doctorRequestRepository.existsByCpf(dto.cpf())){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma solicitação processada com essas informações.");
    }

    doctorRequestRepository.save(new DoctorRequest(dto));

    emailService.sendDoctorRequest(dto);
  }
}
