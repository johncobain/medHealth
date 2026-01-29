package br.edu.ifba.inf012.medHealthAPI.services.validations;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentFormDto;
import br.edu.ifba.inf012.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf012.medHealthAPI.models.enums.DoctorStatus;
import br.edu.ifba.inf012.medHealthAPI.repositories.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ActiveDoctorValidator implements AppointmentValidator {
  private final DoctorRepository doctorRepository;

  public ActiveDoctorValidator(DoctorRepository doctorRepository) {
    this.doctorRepository = doctorRepository;
  }

  @Override
  public void validate(AppointmentFormDto appointmentFormDto) {
    if (appointmentFormDto.doctorId() == null) {
      return;
    }
    Doctor doctor = doctorRepository.findById(appointmentFormDto.doctorId())
        .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), appointmentFormDto.doctorId()));

    if(doctor.getStatus() != DoctorStatus.ACTIVE){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível agendar consulta para um médico inativo.");
    }
  }
}
