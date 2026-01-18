package br.edu.ifba.inf015.medHealthAPI.services.validations;

import br.edu.ifba.inf015.medHealthAPI.dtos.appointment.AppointmentFormDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
public class ClinicHoursValidator implements AppointmentValidator{
  @Override
  public void validate(AppointmentFormDto appointmentFormDto) {
    LocalDateTime appointmentDate = appointmentFormDto.date().toLocalDateTime();
    DayOfWeek dayOfWeek = appointmentDate.getDayOfWeek();
    int hour = appointmentDate.getHour();

    if (dayOfWeek == DayOfWeek.SUNDAY || hour < 7 || hour >=19){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment outside clinic opening hours (Mon-Sat, 7am-7pm).");
    }
  }
}
