package br.edu.ifba.inf012.medHealthAPI.repositories;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf012.medHealthAPI.models.enums.DoctorStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
  @Query("SELECT d FROM Doctor d WHERE d.person.email = :email")
  Optional<Doctor> findByPersonEmail(String email);

  @Query("SELECT d FROM Doctor d WHERE d.person.cpf = :cpf")
  Optional<Doctor> findByPersonCpf(String cpf);

  @Query("SELECT d FROM Doctor d WHERE d.person.id = :personId")
  Optional<Doctor> findByPersonId(Long personId);

  Optional<Doctor> findByCrm(String crm);

  boolean existsByCrm(String crm);

  boolean existsByPersonId(Long personId);

  Page<Doctor> findBySpecialty(Specialty specialty, Pageable pageable);

  List<Doctor> findAllActiveBySpecialty(Specialty specialty);

  Page<Doctor> findByStatus(DoctorStatus status, Pageable pageable);

  Page<Doctor> findBySpecialtyAndStatus(Specialty specialty, DoctorStatus status, Pageable pageable);

  @Query("SELECT d FROM Doctor d WHERE d.id = :id AND d.status = 'ACTIVE'")
  Optional<Doctor> findActiveById(Long id);
}
