package br.edu.ifba.inf012.medHealthAPI.services.validations;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentFormDto;
import br.edu.ifba.inf012.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Patient;
import br.edu.ifba.inf012.medHealthAPI.models.enums.PatientStatus;
import br.edu.ifba.inf012.medHealthAPI.repositories.PatientRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ActivePatientValidator implements AppointmentValidator {
  private final PatientRepository patientRepository;

  public ActivePatientValidator(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  @Override
  public void validate(AppointmentFormDto appointmentFormDto) {
    Patient patient = patientRepository.findById(appointmentFormDto.patientId())
        .orElseThrow(() -> new EntityNotFoundException(Patient.class.getSimpleName(), appointmentFormDto.patientId()));

    if(patient.getStatus() != PatientStatus.ACTIVE){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível agendar consulta para um paciente inativo.");
    }
  }
}
