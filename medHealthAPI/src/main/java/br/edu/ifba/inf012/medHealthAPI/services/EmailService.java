package br.edu.ifba.inf012.medHealthAPI.services;

import org.springframework.stereotype.Service;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.email.EmailDto;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Cancellation;
import br.edu.ifba.inf012.medHealthAPI.producers.EmailProducer;

@Service
public class EmailService {
  private final EmailProducer emailProducer;
  
  public EmailService(EmailProducer emailProducer) {
    this.emailProducer = emailProducer;
  }

  public void sendUserRegistrationEmail(String emailTo, String userName) {
    String subject = "Cadastro realizado com sucesso - Bem Vindo ao MedHealth!";
    String text = "Olá " + userName + ",\n\n" +
                  "Seu cadastro no MedHealth foi realizado com sucesso!\n" +
                  "Agora você pode utilizar o nosso sistema de agendamento de consultas.\n\n" +
                  "Atenciosamente,\n" +
                  "Equipe MedHealth";
    EmailDto emailDto = new EmailDto(emailTo, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

  public void sendAppointmentConfirmationToPatient(String patientEmail, AppointmentDto appointment) {}

  public void sendAppointmentConfirmationToDoctor(String doctorEmail, AppointmentDto appointment) {}
  
  public void sendAppointmentCancelationToPatient(String patientEmail, AppointmentDto appointment, Cancellation cancellation) {}
  
  public void sendAppointmentCancelationToDoctor(String doctorEmail, AppointmentDto appointment, Cancellation cancellation) {}
  
  public void sendAppointmentAttendanceToPatient(String patientEmail, AppointmentDto appointment) {}

  public void sendAppointmentAttendanceToDoctor(String doctorEmail, AppointmentDto appointment) {}

}
