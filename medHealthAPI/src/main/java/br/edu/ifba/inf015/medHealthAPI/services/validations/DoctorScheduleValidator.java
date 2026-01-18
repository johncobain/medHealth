package br.edu.ifba.inf015.medHealthAPI.services.validations;

import br.edu.ifba.inf015.medHealthAPI.dtos.appointment.AppointmentFormDto;
import br.edu.ifba.inf015.medHealthAPI.repositories.AppointmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class DoctorScheduleValidator implements AppointmentValidator{
  private final AppointmentRepository appointmentRepository;

  public DoctorScheduleValidator(AppointmentRepository appointmentRepository){
    this.appointmentRepository = appointmentRepository;
  }

  @Override
  public void validate(AppointmentFormDto appointmentFormDto) {
    if (appointmentFormDto.doctorId() == null){
      return;
    }
    boolean doctorIsBusy = appointmentRepository.existsByDoctorIdAndDate(appointmentFormDto.doctorId(), appointmentFormDto.date());
    if (doctorIsBusy){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor already has an appointment at this time.");
    }
  }
}
