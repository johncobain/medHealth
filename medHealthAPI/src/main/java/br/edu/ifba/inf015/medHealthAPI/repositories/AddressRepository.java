package br.edu.ifba.inf015.medHealthAPI.repositories;

import br.edu.ifba.inf015.medHealthAPI.models.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
