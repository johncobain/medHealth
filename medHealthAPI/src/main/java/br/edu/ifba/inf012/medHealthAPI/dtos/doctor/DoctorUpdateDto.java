package br.edu.ifba.inf012.medHealthAPI.dtos.doctor;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressFormDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

public record DoctorUpdateDto(
        @Schema(description = "Nome completo", example = "Marcos Vinicius")
        String fullName,

        @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone inválido")
        @Schema(description = "Telefone", example = "(99) 99999-9999")
        String phone,

        @Schema(description = "Endereço")
        AddressFormDto address
) {}
