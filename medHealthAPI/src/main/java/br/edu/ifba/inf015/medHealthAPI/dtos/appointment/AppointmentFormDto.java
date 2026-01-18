package br.edu.ifba.inf015.medHealthAPI.dtos.appointment;

import br.edu.ifba.inf015.medHealthAPI.models.entities.Appointment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record AppointmentFormDto(
        @NotNull(message = "Date is required")
        @Future(message = "Date must be in the future")
        @Schema(description = "Appointment date", example = "2026-02-25T15:00:00.000Z")
        Timestamp date,

        @Schema(description = "Doctor id", example = "1")
        Long doctorId,

        @NotNull(message = "Patient ID is required")
        @Schema(description = "Patient id", example = "1")
        Long patientId,

        @Schema(description = "Required if doctorId is not provided", example = "CARDIOLOGY")
        String specialty
) {
    public AppointmentFormDto(Appointment appointment) {
        this(
                appointment.getDate(),
                appointment.getDoctor().getId(),
                appointment.getPatient().getId(),
                appointment.getDoctor().getSpecialty().name()
        );
    }
}
