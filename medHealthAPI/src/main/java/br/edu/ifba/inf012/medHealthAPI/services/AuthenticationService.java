package br.edu.ifba.inf012.medHealthAPI.services;

import br.edu.ifba.inf012.medHealthAPI.dtos.user.AuthenticationDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.user.LoginResponseDto;
import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import br.edu.ifba.inf012.medHealthAPI.repositories.DoctorRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.PatientRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PatientRepository patientRepository;

  @Autowired
  private DoctorRepository doctorRepository;

  @Autowired
  private JWTTokenService tokenService;

  @Autowired
  @Lazy
  private AuthenticationManager authenticationManager;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByPersonEmail(email)
      .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
  }

  public LoginResponseDto login(AuthenticationDto dto){
    var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
    Authentication auth = this.authenticationManager.authenticate(usernamePassword);

    User user = (User) auth.getPrincipal();
    var token = this.tokenService.generateToken(user);

    String role = user.getRoles().stream().findFirst().get().getAuthority();;
    Long specificId = null;

    if(role.equals("ROLE_PATIENT")){
      specificId = patientRepository.findByPersonId(user.getPerson().getId()).get().getId();
          
    }else if(role.equals("ROLE_DOCTOR")){
      specificId = doctorRepository.findByPersonId(user.getPerson().getId()).get().getId();
    }else{
      specificId = 0L; // Admin role
    }

    return new LoginResponseDto(
        token,
        role,
        user.getId(),
        specificId,
        user.getPerson().getFullName(),
        user.getPerson().getEmail()
      );
  }
}
