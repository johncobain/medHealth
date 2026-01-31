package br.edu.ifba.inf012.medHealthAPI.repositories;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Patient;
import br.edu.ifba.inf012.medHealthAPI.models.enums.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
  @Query("SELECT p FROM Patient p WHERE p.person.email = :email")
  Optional<Patient> findByPersonEmail(String email);

  @Query("SELECT p FROM Patient p WHERE p.person.cpf = :cpf")
  Optional<Patient> findByPersonCpf(String cpf);

  @Query("SELECT p FROM Patient p WHERE p.person.id = :personId")
  Optional<Patient> findByPersonId(Long personId);

  boolean existsByPersonId(Long personId);

  Page<Patient> findByStatus(PatientStatus status, Pageable pageable);

  @Query("SELECT p FROM Patient p WHERE p.id = :id AND p.status = 'ACTIVE'")
  Optional<Patient> findActiveById(Long id);
}
