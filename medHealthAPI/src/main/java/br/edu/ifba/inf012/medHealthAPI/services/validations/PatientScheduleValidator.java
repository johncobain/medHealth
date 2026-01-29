package br.edu.ifba.inf012.medHealthAPI.services.validations;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentFormDto;
import br.edu.ifba.inf012.medHealthAPI.repositories.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PatientScheduleValidator implements AppointmentValidator{
  private final AppointmentRepository appointmentRepository;

  public PatientScheduleValidator(AppointmentRepository appointmentRepository) {
    this.appointmentRepository = appointmentRepository;
  }

  @Override
  public void validate(AppointmentFormDto appointmentFormDto) {
   boolean patientHasAppointment = appointmentRepository.existsByPatientIdAndDate(appointmentFormDto.patientId(), appointmentFormDto.date());
   if (patientHasAppointment){
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Paciente já possui outra consulta agendada nesse horário.");
   }
  }
}
