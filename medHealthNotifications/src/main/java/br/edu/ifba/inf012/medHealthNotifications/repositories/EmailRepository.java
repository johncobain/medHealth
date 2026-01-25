package br.edu.ifba.inf012.medHealthNotifications.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifba.inf012.medHealthNotifications.models.entities.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {
}