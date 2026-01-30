package br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest;

import br.edu.ifba.inf012.medHealthAPI.models.enums.DoctorRequestStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record DoctorRequestFormDto(
    @NotBlank(message = "O nome completo é obrigatório")
    String fullName,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    String email,

    @NotBlank(message = "O telefone é obrigatório")
    @Size(max = 18)
    String phone,

    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}", message = "Formato de CPF inválido") // Exemplo de regex
    String cpf,

    @NotBlank(message = "O CRM é obrigatório")
    String crm,

    @NotNull(message = "A especialidade é obrigatória")
    Specialty specialty,
    
    @NotNull(message = "O status é obrigatório")
    DoctorRequestStatus status,

    @NotBlank(message = "O estado é obrigatório")
    String state,

    @NotBlank(message = "A cidade é obrigatória")
    String city,

    @NotBlank(message = "O bairro é obrigatório")
    String neighborhood,

    @NotBlank(message = "A rua é obrigatória")
    String street,

    String number,

    String complement,

    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Formato de CEP inválido")
    String zipCode
) {}
