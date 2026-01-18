package br.edu.ifba.inf015.medHealthAPI.services.validations;

import br.edu.ifba.inf015.medHealthAPI.dtos.appointment.AppointmentFormDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class AdvanceNoticeValidator implements AppointmentValidator {
  @Override
  public void validate(AppointmentFormDto appointmentFormDto) {
    LocalDateTime appointmentDate = appointmentFormDto.date().toLocalDateTime();
    LocalDateTime now = LocalDateTime.now();
    long minutes = Duration.between(now, appointmentDate).toMinutes();

    if (minutes < 30){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment must be scheduled at least 30 minutes in advance.");
    }
  }
}
