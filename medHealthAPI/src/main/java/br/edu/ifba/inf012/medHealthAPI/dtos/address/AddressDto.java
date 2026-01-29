package br.edu.ifba.inf012.medHealthAPI.dtos.address;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

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

  public static AddressDto fromEntity(Address address) {
    return new AddressDto(address);
  }
}
