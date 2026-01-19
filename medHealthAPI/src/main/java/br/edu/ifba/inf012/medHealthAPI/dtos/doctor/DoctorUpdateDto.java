package br.edu.ifba.inf012.medHealthAPI.dtos.doctor;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressFormDto;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import io.swagger.v3.oas.annotations.media.Schema;

public record DoctorUpdateDto(
        @Schema(description = "Doctor name", example = "Mark Smith")
        String name,
        @Schema(description = "Doctor phone", example = "123456789")
        String phone,
        @Schema(description = "Doctor address")
        AddressFormDto address
) {
    public DoctorUpdateDto(Doctor doctor) {
        this(
            doctor.getName(),
            doctor.getPhone(),
            new AddressFormDto(doctor.getAddress())
        );
    }
}
