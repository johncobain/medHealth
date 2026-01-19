package br.edu.ifba.inf012.medHealthAPI.dtos.patient;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressFormDto;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Patient;
import io.swagger.v3.oas.annotations.media.Schema;

public record PatientUpdateDto(
        @Schema(description = "Patient name", example = "John Doe")
        String name,
        @Schema(description = "Patient phone", example = "+55 71 99999-9999")
        String phone,
        @Schema(description = "Patient address")
        AddressFormDto address
) {
  public PatientUpdateDto(Patient patient){
    this(patient.getName(),
      patient.getPhone(),
      new AddressFormDto(patient.getAddress())
    );
  }
}
