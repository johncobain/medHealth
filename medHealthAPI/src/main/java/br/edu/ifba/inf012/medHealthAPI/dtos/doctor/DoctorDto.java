package br.edu.ifba.inf012.medHealthAPI.dtos.doctor;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;

public record DoctorDto(
        Long id,
        String name,
        String email,
        String crm,
        Specialty specialty
) {
    public DoctorDto(Doctor doctor){
        this(
                doctor.getId(),
                doctor.getName(),
                doctor.getEmail(),
                doctor.getCrm(),
                doctor.getSpecialty()
        );
    }

    public static DoctorDto fromEntity(Doctor doctor){
        return new DoctorDto(doctor);
    }
}
