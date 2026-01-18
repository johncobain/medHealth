package br.edu.ifba.inf015.medHealthAPI.services.validations;

import br.edu.ifba.inf015.medHealthAPI.models.entities.Appointment;

public interface CancellationValidator {
  void validate(Appointment appointment);
}
