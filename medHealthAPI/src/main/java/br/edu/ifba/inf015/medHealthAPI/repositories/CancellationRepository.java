package br.edu.ifba.inf015.medHealthAPI.repositories;

import br.edu.ifba.inf015.medHealthAPI.models.entities.Cancellation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancellationRepository extends JpaRepository<Cancellation, Long> {
}
