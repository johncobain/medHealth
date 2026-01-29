package br.edu.ifba.inf012.medHealthAPI.repositories;

import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPersonEmail(String email);
    Optional<User> findByPasswordResetToken(String token);

    boolean existsByPersonId(Long id);
}
