package br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest;

import br.edu.ifba.inf012.medHealthAPI.models.entities.DoctorRequest;
import br.edu.ifba.inf012.medHealthAPI.models.enums.DoctorRequestStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;

public record DoctorRequestDto(
    Long id,

    String fullName,

    String email,

    String phone,

    String cpf,

    String crm,

    Specialty specialty,
    
    DoctorRequestStatus status,

    String state,

    String city,

    String neighborhood,

    String street,

    String number,

    String complement,

    String zipCode
) {

    public DoctorRequestDto(DoctorRequest doctorRequest){
        this(
            doctorRequest.getId(),
            doctorRequest.getFullName(),
            doctorRequest.getEmail(),
            doctorRequest.getPhone(),
            doctorRequest.getCpf(),
            doctorRequest.getCrm(),
            doctorRequest.getSpecialty(),
            doctorRequest.getStatus(),
            doctorRequest.getState(),
            doctorRequest.getCity(),
            doctorRequest.getNeighborhood(),
            doctorRequest.getStreet(),
            doctorRequest.getNumber(),
            doctorRequest.getComplement(),
            doctorRequest.getZipCode()
        );
    }

    public static DoctorRequestDto fromEntity(DoctorRequest doctorRequest){
        return new DoctorRequestDto(doctorRequest);
    }

}