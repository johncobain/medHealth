package br.edu.ifba.inf012.medHealthAPI.dtos.user;

public record LoginResponseDto(
  String token,
  String role,
  Long userId,
  Long specificId,
  String fullName,
  String email
) {
}
