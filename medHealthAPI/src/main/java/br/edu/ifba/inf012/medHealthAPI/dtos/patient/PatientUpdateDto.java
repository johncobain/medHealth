package br.edu.ifba.inf012.medHealthAPI.dtos.patient;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressFormDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record PatientUpdateDto(
        @Schema(description = "Patient fullName", example = "John Doe")
        String fullName,
        @Schema(description = "Patient phone", example = "+55 71 99999-9999")
        String phone,
        @Schema(description = "Patient address")
        AddressFormDto address
) {}
