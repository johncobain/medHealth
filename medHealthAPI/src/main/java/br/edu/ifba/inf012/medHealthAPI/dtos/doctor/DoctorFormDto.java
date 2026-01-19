package br.edu.ifba.inf012.medHealthAPI.dtos.doctor;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressFormDto;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record DoctorFormDto(
        @NotNull(message = "Name is required")
        @Schema(description = "Doctor name", example = "Mark Smith")
        String name,
        @NotNull(message = "Email is required")
        @Schema(description = "Doctor email", example = "mark.smith@example.com")
        String email,
        @NotNull(message = "Phone is required")
        @Schema(description = "Doctor phone", example = "123456789")
        String phone,
        @NotNull(message = "CRM is required")
        @Schema(description = "Doctor CRM", example = "123456")
        String crm,
        @NotNull(message = "Address is required")
        @Schema(description = "Doctor address")
        AddressFormDto address,
        @NotNull(message = "Specialty is required")
        @Schema(description = "Doctor specialty", example = "ORTHOPEDIC")
        String specialty
) {
    public DoctorFormDto(Doctor doctor) {
        this(doctor.getName(), doctor.getEmail(), doctor.getPhone(), doctor.getCrm(), new AddressFormDto(doctor.getAddress()), doctor.getSpecialty().name());
    }
}
