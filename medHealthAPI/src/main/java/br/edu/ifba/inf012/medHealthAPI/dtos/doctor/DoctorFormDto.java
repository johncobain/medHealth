package br.edu.ifba.inf012.medHealthAPI.dtos.doctor;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressFormDto;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record DoctorFormDto(
  @NotBlank(message = "Nome completo é obrigatório")
  @Schema(description = "Nome completo", example = "Marcos Vinicius")
  String fullName,

  @NotBlank(message = "Email é obrigatório")
  @Email(message = "Email inválido")
  @Schema(description = "Email", example = "marcos.vinicius@example.com")
  String email,

  @NotBlank(message = "Telefone é obrigatório")
  @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Telefone inválido")
  @Schema(description = "Telefone", example = "(99) 99999-9999")
  String phone,

  @NotBlank(message = "CPF é obrigatório")
  @CPF
  @Schema(description = "CPF", example = "677.826.450-00")
  String cpf,

  @NotBlank(message = "CRM é obrigatório")
  @Pattern(regexp = "CRM-[A-Z]{2}-\\d{4,6}", message = "CRM deve estar no formato CRM-UF-NUMERO (ex: CRM-SP-12345)")
  @Schema(description = "CRM", example = "CRM-SP-12345")
  String crm,

  @NotNull(message = "Endereço é obrigatório")
  @Schema(description = "Endereço")
  AddressDto address,

  @NotNull(message = "Especialidade é obrigatória")
  @Schema(description = "Especialidade", example = "CARDIOLOGY")
  Specialty specialty
) {}
