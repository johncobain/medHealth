package br.edu.ifba.inf015.medHealthAPI.dtos.cancelation;

import br.edu.ifba.inf015.medHealthAPI.models.enums.CancellationReason;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CancellationFormDto (
    @NotNull(message = "Reason is required")
    @Schema(description = "Reason for cancellation", example = "PATIENT_CANCELED")
    CancellationReason reason,

    @Schema(description = "Additional message if reason is 'OUTROS'", example = "O paciente não poderá comparecer por conta de imprevistos.")
    String message
){
}
