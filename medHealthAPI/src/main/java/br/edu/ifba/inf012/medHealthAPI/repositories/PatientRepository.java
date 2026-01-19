package br.edu.ifba.inf012.medHealthAPI.repositories;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByStatus(Pageable pageable, String status);
    Page<Patient> findByNameContainingAndStatus(Pageable pageable, String name, String status);
    Patient findByIdAndStatus(Long id, String status);
    Patient findByCpfAndStatus(String cpf, String status);
    Patient findByEmailAndStatus(String email, String status);

    boolean existsByCpf(String cpf);
    boolean existsByCpfAndStatus(String cpf, String status);
}
