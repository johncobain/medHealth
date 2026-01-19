package br.edu.ifba.inf012.medHealthAPI.repositories;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
  Page<Doctor> findByStatus(Pageable pageable, String status);
  Page<Doctor> findByNameContainingAndStatus(Pageable pageable, String name, String status);
  Doctor findByIdAndStatus(Long id, String status);
  Doctor findByCrmAndStatus(String crm, String status);
  Doctor findByEmailAndStatus(String email, String status);

  boolean existsByCrm(String crm);
  boolean existsByCrmAndStatus(String crm, String status);

  @Query("SELECT d FROM Doctor d WHERE d.status = 'ACTIVE' AND d.specialty = :specialty")
  List<Doctor> findAllActiveBySpecialty(String specialty);
}
