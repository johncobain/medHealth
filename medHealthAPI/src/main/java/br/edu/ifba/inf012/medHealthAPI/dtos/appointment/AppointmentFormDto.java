package br.edu.ifba.inf012.medHealthAPI.dtos.appointment;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Appointment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public record AppointmentFormDto(
        @NotNull(message = "Data é obrigatória")
        @Future(message = "Data deve ser no futuro")
        @Schema(description = "Data", example = "2026-02-25T15:00:00.000Z")
        Timestamp date,

        @Schema(description = "Id do médico", example = "1")
        Long doctorId,

        @NotNull(message = "Id do paciente é obrigatório")
        @Schema(description = "Id do paciente", example = "1")
        Long patientId,

        @Schema(description = "Obricatório caso médico não seja informado", example = "CARDIOLOGY")
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
