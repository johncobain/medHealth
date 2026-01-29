package br.edu.ifba.inf012.medHealthAPI.dtos.patient;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Patient;

public record PatientDto(
        Long id,
        String fullName,
        String email,
        String cpf
) {
    public PatientDto(Patient patient){
        this(patient.getId(), patient.getPerson().getFullName(), patient.getPerson().getEmail(), patient.getPerson().getCpf());
    }

    public static PatientDto fromEntity(Patient patient){
        return new PatientDto(patient);
    }
}
