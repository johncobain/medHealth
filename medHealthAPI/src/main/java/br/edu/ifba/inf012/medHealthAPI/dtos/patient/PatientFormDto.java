package br.edu.ifba.inf012.medHealthAPI.dtos.patient;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.br.CPF;

public record PatientFormDto(
      @NotBlank(message = "Nome completo é obrigatório")
      @Schema(description = "Nome completo", example = "João Victor")
      String fullName,

      @NotBlank(message = "Email é obrigatório")
      @Email(message = "Email inválido")
      @Schema(description = "Email", example = "joao.vitor@example.com")
      String email,

      @NotBlank(message = "Telefone é obrigatório")
      @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone deve estar no formato (99) 99999-9999")
      @Schema(description = "Telefone", example = "(99) 99999-9999")
      String phone,

      @NotBlank(message = "CPF é obrigatório")
      @CPF
      @Schema(description = "CPF", example = "677.826.450-00")
      String cpf,

      @NotNull(message = "Endereço é obrigatório")
      @Schema(description = "Endereço")
      AddressDto address
) {}
