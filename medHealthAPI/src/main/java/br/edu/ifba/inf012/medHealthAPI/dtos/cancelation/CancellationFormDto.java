package br.edu.ifba.inf012.medHealthAPI.dtos.cancelation;

import br.edu.ifba.inf012.medHealthAPI.models.enums.CancellationReason;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CancellationFormDto (
    @NotNull(message = "Motivo é obrigatório")
    @Schema(description = "Motivo do cancelamento", example = "PATIENT_CANCELED")
    CancellationReason reason,

    @Schema(description = "Mensagem adicional (obrigatória caso motivo seja OUTROS)", example = "O paciente não poderá comparecer por conta de imprevistos.")
    String message
){
}
