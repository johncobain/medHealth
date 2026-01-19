package br.edu.ifba.inf012.medHealthAPI.services.validations;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentFormDto;

public interface AppointmentValidator {
  void validate(AppointmentFormDto appointmentFormDto);
}
