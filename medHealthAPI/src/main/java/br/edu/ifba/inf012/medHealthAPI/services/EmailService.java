package br.edu.ifba.inf012.medHealthAPI.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.email.EmailDto;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Cancellation;
import br.edu.ifba.inf012.medHealthAPI.producers.EmailProducer;

@Service
public class EmailService {
  private final EmailProducer emailProducer;
  private final DateTimeFormatter dateTimeFormatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private final DateTimeFormatter dateTimeFormatterHour = DateTimeFormatter.ofPattern("HH:mm");
  
  public EmailService(EmailProducer emailProducer) {
    this.emailProducer = emailProducer;
  }

  public void sendUserRegistrationEmail(String emailTo, String userName) {
    String subject = "Cadastro realizado com sucesso - Bem Vindo ao MedHealth!";
    String text = "Olá, " + userName + ",\n\n" +
                  "Seu cadastro no MedHealth foi realizado com sucesso!\n" +
                  "Agora você pode utilizar o nosso sistema de agendamento de consultas.\n\n" +
                  "Atenciosamente,\n" +
                  "Equipe MedHealth";
    EmailDto emailDto = new EmailDto(emailTo, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

  public void sendAppointmentConfirmationToPatient(String patientEmail, AppointmentDto appointment) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta marcada com sucesso!";
    String text = "Olá, " + appointment.patient().name() + ",\n\n" +
                  "Sua consulta no MedHealth com o doutor " + appointment.doctor().name() + " foi marcada para a data "+ date + "às "+ hour +"\n" +
                  "Caso não possa comparecer, pedimos a gentileza de avisar com antecedência.\n" +
                  "Ficamos à disposição para qualquer dúvida.\n\n" +
                  "Atenciosamente,\n" +
                  "Equipe MedHealth";
    EmailDto emailDto = new EmailDto(patientEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

  public void sendAppointmentConfirmationToDoctor(String doctorEmail, AppointmentDto appointment) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta marcada com sucesso!";
    String text = "Olá, Dt. " + appointment.doctor().name() + ",\n\n" +
                  "Sua consulta no MedHealth com o paciente " + appointment.patient().name() + " foi marcada para a data "+ date + " às "+ hour +"\n" +
                  "Caso não possa comparecer, pedimos a gentileza de avisar com antecedência.\n" +
                  "Ficamos à disposição para qualquer dúvida.\n\n" +
                  "Atenciosamente,\n" +
                  "Equipe MedHealth";
    EmailDto emailDto = new EmailDto(doctorEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }
  
  public void sendAppointmentCancelationToPatient(String patientEmail, AppointmentDto appointment, Cancellation cancellation) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta cancelada!";
    String text = "Olá, " + appointment.patient().name() + ",\n\n" +
                  "Sua consulta no MedHealth com o Dr. " + appointment.doctor().name() + ", marcada para a data "+ date + " às "+ hour + " foi cancelada.\n" +
                  "Motivo: " + cancellation.getReason().toString() + "\n" +
                  (cancellation.getMessage().isEmpty() ? "" : "Mensagem: "+cancellation.getMessage() + "\n") +
                  "Ficamos à disposição para qualquer dúvida.\n\n" +
                  "Atenciosamente,\n" +
                  "Equipe MedHealth";
    EmailDto emailDto = new EmailDto(patientEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }
  
  public void sendAppointmentCancelationToDoctor(String doctorEmail, AppointmentDto appointment, Cancellation cancellation) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);
    
    String subject = "Consulta cancelada!";
    String text = "Olá, Dr. " + appointment.doctor().name() + ",\n\n" +
                  "Sua consulta no MedHealth com o paciente " + appointment.patient().name() + ", marcada para a data "+ date + " às "+ hour + " foi cancelada.\n" +
                  "Motivo: " + cancellation.getReason().toString() + "\n" +
                  (cancellation.getMessage().isEmpty() ? "" : "Mensagem: "+cancellation.getMessage() + "\n") +
                  "Ficamos à disposição para qualquer dúvida.\n\n" +
                  "Atenciosamente,\n" +
                  "Equipe MedHealth";
    EmailDto emailDto = new EmailDto(doctorEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }
  
  public void sendAppointmentAttendanceToPatient(String patientEmail, AppointmentDto appointment) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta realizada com sucesso!";
    String text = "Olá, " + appointment.patient().name() + ",\n\n" +
                  "Sua consulta no MedHealth com o Dr. " + appointment.doctor().name() + " para a data "+ date + "às "+ hour +" foi concluída!\n" +
                  "Agradecemos a preferência.\n\n" +
                  "Atenciosamente,\n" +
                  "Equipe MedHealth";
    EmailDto emailDto = new EmailDto(patientEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

  public void sendAppointmentAttendanceToDoctor(String doctorEmail, AppointmentDto appointment) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta realizada com sucesso!";
    String text = "Olá, Dr. " + appointment.doctor().name() + ",\n\n" +
                  "Sua consulta no MedHealth com o paciente " + appointment.patient().name() + " para a data "+ date + "às "+ hour +" foi concluída!\n" +
                  "Ficamos à disposição para qualquer dúvida.\n\n" +
                  "Atenciosamente,\n" +
                  "Equipe MedHealth";
    EmailDto emailDto = new EmailDto(doctorEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

}
