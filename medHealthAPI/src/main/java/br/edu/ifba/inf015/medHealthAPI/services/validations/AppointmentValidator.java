package br.edu.ifba.inf015.medHealthAPI.services.validations;

import br.edu.ifba.inf015.medHealthAPI.dtos.appointment.AppointmentFormDto;

public interface AppointmentValidator {
  void validate(AppointmentFormDto appointmentFormDto);
}
