package br.edu.ifba.inf012.medHealthAPI.dtos.dashboard;

public record DashboardStatsDto(
    long totalDoctors,
    long totalPatients,
    long totalAppointments,
    long pendingAppointments,
    long todayAppointments
) {}