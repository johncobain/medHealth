package br.edu.ifba.inf012.medHealthAPI.services.validations;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Appointment;
import br.edu.ifba.inf012.medHealthAPI.models.enums.AppointmentStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AlreadyCancelledCancellationValidatoor implements CancellationValidator {

    @Override
    public void validate(Appointment appointment) {
        if(appointment.getStatus().equals(AppointmentStatus.CANCELED)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment has already been canceled.");
        }
    }
}