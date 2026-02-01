package br.edu.ifba.inf012.medHealthAPI.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations {
  @Autowired
  private SecurityFilter securityFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .csrf(csrf -> csrf.disable())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(req -> req
        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/request-register").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/forgot-password").permitAll()
        .requestMatchers(HttpMethod.POST, "/auth/reset-password").permitAll()

        .requestMatchers(HttpMethod.GET, "/doctors/getSpecialties").permitAll()
        .requestMatchers(HttpMethod.GET, "/appointments/getCancellationReasons").permitAll()
        
        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/").permitAll()
        
        .requestMatchers(HttpMethod.GET, "/patients/me").hasAnyRole("PATIENT")
        .requestMatchers(HttpMethod.PUT, "/patients/me").hasAnyRole("PATIENT")
        .requestMatchers(HttpMethod.GET, "/doctors/me").hasAnyRole("DOCTOR") 
        .requestMatchers(HttpMethod.PUT, "/doctors/me").hasAnyRole("DOCTOR") 

        .requestMatchers(HttpMethod.GET, "/doctors/**").hasAnyRole("ADMIN", "PATIENT", "DOCTOR") 
        .requestMatchers(HttpMethod.GET, "/patients/**").hasAnyRole("ADMIN", "DOCTOR")
        .requestMatchers(HttpMethod.POST, "/appointments").hasAnyRole("ADMIN", "PATIENT", "DOCTOR")

        .requestMatchers(HttpMethod.POST, "/patients").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/doctors").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/patients/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/doctors/**").hasRole("ADMIN")

        .requestMatchers(HttpMethod.POST, "/doctors-request/accept/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/doctors-request/decline/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/doctors-request/**").hasRole("ADMIN")
        
        .anyRequest().authenticated()
      )
      .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
