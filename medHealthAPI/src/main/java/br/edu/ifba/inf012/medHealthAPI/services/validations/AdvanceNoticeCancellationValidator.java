package br.edu.ifba.inf012.medHealthAPI.services.validations;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Appointment;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;

public class AdvanceNoticeCancellationValidator implements CancellationValidator {
  @Override
  public void validate(Appointment appointment) {
    LocalDateTime appointmentDate = appointment.getDate().toLocalDateTime();
    LocalDateTime now = LocalDateTime.now();
    long hours = Duration.between(now, appointmentDate).toHours();

    if(hours < 24){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cancellation must be made at least 24 hours in advance.");
    }
  }
}
