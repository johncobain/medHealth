package br.edu.ifba.inf012.medHealthAPI.dtos.appointment;

import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.patient.PatientDto;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Appointment;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public record AppointmentDto (
    Long id,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    Timestamp date,
    DoctorDto doctor,
    PatientDto patient,
    String status
){
    public AppointmentDto(Appointment appointment){
        this(appointment.getId(), appointment.getDate(), DoctorDto.fromEntity(appointment.getDoctor()), PatientDto.fromEntity(appointment.getPatient()), appointment.getStatus());
    }

    public static AppointmentDto fromEntity(Appointment appointment){
        return new AppointmentDto(appointment);
    }
}
