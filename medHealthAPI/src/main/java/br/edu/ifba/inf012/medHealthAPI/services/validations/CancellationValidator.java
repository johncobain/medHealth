package br.edu.ifba.inf012.medHealthAPI.services.validations;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Appointment;

public interface CancellationValidator {
  void validate(Appointment appointment);
}
