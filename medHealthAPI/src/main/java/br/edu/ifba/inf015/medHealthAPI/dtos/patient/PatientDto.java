package br.edu.ifba.inf015.medHealthAPI.dtos.patient;

import br.edu.ifba.inf015.medHealthAPI.models.entities.Patient;

public record PatientDto(
        Long id,
        String name,
        String email,
        String cpf
) {
    public PatientDto(Patient patient){
        this(patient.getId(), patient.getName(), patient.getEmail(), patient.getCpf());
    }

    public static PatientDto fromEntity(Patient patient){
        return new PatientDto(patient);
    }
}
