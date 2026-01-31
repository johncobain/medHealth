package br.edu.ifba.inf012.medHealthAPI.dtos.address;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest.DoctorRequestDto;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Address;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AddressDto(
  Long id,
  String state,
  String city,
  String neighborhood,
  String street,
  String number,
  String complement,
  String zipCode
) {

  public AddressDto(Address address){
    this(address.getId(),
         address.getState(),
         address.getCity(),
         address.getNeighborhood(),
         address.getStreet(),
         address.getNumber(),
         address.getComplement(),
         address.getZipCode()
         );
  }
  
  public AddressDto(DoctorRequestDto dto){
    this(null,
         dto.state(),
         dto.city(),
         dto.neighborhood(),
         dto.street(),
         dto.number(),
         dto.complement(),
         dto.zipCode()
         );
  }

  public static AddressDto fromEntity(Address address) {
    return new AddressDto(address);
  }
}
