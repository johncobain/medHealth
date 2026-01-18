package br.edu.ifba.inf015.medHealthAPI.dtos.address;

import br.edu.ifba.inf015.medHealthAPI.models.entities.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AddressFormDto (
  @NotNull(message = "State must not be null")
  @Schema(description = "State of the address", example = "Bahia")
  String state,
  @NotNull(message = "City must not be null")
  @Schema(description = "City of the address", example = "Salvador")
  String city,
  @NotNull(message = "Neighborhood must not be null")
  @Schema(description = "Neighborhood of the address", example = "Centro")
  String neighborhood,
  @NotNull(message = "Street must not be null")
  @Schema(description = "Street of the address", example = "Avenida Sete de Setembro")
  String street,
  @Schema(description = "Number of the address", example = "123")
  String number,
  @Schema(description = "Complement of the address", example = "Apto 101")
  String complement,
  @NotNull(message = "ZIP code must not be null")
  @Pattern(regexp = "^[0-9]{5}-?[0-9]{3}$", message = "Invalid CEP format")
  @Schema(description = "ZIP code of the address", example = "40000-000")
  String zipCode
){

  public AddressFormDto (Address address){
    this(address.getState(),
         address.getCity(),
         address.getNeighborhood(),
         address.getStreet(),
         address.getNumber(),
         address.getComplement(),
         address.getZipCode()
         );
  }
  
}
