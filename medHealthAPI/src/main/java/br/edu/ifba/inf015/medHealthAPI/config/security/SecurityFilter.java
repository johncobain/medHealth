package br.edu.ifba.inf015.medHealthAPI.config.security;

import br.edu.ifba.inf015.medHealthAPI.models.entities.User;
import br.edu.ifba.inf015.medHealthAPI.repositories.UserRepository;
import br.edu.ifba.inf015.medHealthAPI.services.JWTTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);

        if(token != null){
            String login = tokenService.getSubject(token);
            UserDetails user = userRepository.findByEmail(login);
            if(user != null){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            System.out.println("Login: " + login);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token == null || !token.startsWith("Bearer")){
            return null;
        }
        return token.replace("Bearer ", "");
    }
}
