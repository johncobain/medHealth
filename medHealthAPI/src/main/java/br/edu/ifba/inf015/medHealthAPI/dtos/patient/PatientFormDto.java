package br.edu.ifba.inf015.medHealthAPI.dtos.patient;

import br.edu.ifba.inf015.medHealthAPI.dtos.address.AddressFormDto;
import br.edu.ifba.inf015.medHealthAPI.models.entities.Patient;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record PatientFormDto(
        @NotNull(message = "Name is required")
        @Schema(description = "Patient name", example = "John Doe")
        String name,
        @NotNull(message = "Email is required")
        @Schema(description = "Patient email", example = "john.doe@example.com")
        String email,
        @NotNull(message = "phone is required")
        @Schema(description = "Patient phone", example = "+55 71 99999-9999")
        String phone,
        @NotNull(message = "cpf is required")
        @Schema(description = "Patient cpf", example = "677.826.450-00")
        @CPF
        String cpf,
        @NotNull(message = "address is required")
        @Schema(description = "Patient address")
        AddressFormDto address
) {
    public PatientFormDto(Patient patient){
        this(patient.getName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getCpf(),
                new AddressFormDto(patient.getAddress())
                );
    }
}
