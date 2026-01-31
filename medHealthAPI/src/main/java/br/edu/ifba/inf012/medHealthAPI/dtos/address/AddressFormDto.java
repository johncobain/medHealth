package br.edu.ifba.inf012.medHealthAPI.dtos.address;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddressFormDto (
  @NotBlank(message = "Estado é obrigatório")
  @Schema(description = "Estado", example = "Bahia")
  String state,

  @NotBlank(message = "Cidade é obrigatória")
  @Schema(description = "Cidade", example = "Salvador")
  String city,

  @NotBlank(message = "Bairro é obrigatório")
  @Schema(description = "Bairro", example = "Centro")
  String neighborhood,

  @NotBlank(message = "Rua é obrigatória")
  @Schema(description = "Rua", example = "Avenida Sete de Setembro")
  String street,

  @Schema(description = "Número", example = "123")
  String number,

  @Schema(description = "Complemento", example = "Apto 101")
  String complement,

  @NotBlank(message = "CEP é obrigatório")
  @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve estar no formato 99999-999")
  @Schema(description = "CEP", example = "40000-000")
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
