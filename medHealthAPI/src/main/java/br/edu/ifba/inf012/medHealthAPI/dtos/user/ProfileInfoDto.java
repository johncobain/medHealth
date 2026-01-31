package br.edu.ifba.inf012.medHealthAPI.dtos.user;


public record ProfileInfoDto(
  Long id,
  Long personId,
  String email,
  String fullName,
  String phone,
  String cpf,
  String role
) {
  
}
