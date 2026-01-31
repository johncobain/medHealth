package br.edu.ifba.inf012.medHealthAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifba.inf012.medHealthAPI.models.entities.DoctorRequest;

public interface DoctorRequestRepository extends JpaRepository<DoctorRequest, Long>{

    boolean existsByCpf(String cpf);
    
}
