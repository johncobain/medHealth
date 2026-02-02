package br.edu.ifba.inf012.medHealthAPI.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifba.inf012.medHealthAPI.models.entities.DoctorRequest;
import br.edu.ifba.inf012.medHealthAPI.models.enums.DoctorRequestStatus;

public interface DoctorRequestRepository extends JpaRepository<DoctorRequest, Long>{

    boolean existsByCpf(String cpf);
    
    Page<DoctorRequest> findByStatus(DoctorRequestStatus status, Pageable pageable);
    
}
