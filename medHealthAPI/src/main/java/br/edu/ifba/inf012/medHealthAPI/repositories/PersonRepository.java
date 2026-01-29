package br.edu.ifba.inf012.medHealthAPI.repositories;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
  Optional<Person> findByEmail(String email);
  Optional<Person> findByCpf(String cpf);

  boolean existsByEmail(String email);
  boolean existsByCpf(String cpf);
}
