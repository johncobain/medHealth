package br.edu.ifba.inf012.medHealthAPI.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Person;
import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import br.edu.ifba.inf012.medHealthAPI.models.enums.CancellationReason;
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

  public void sendCredentialsEmail(Person person, String tempPassword, boolean isDoctor) {
    String role = isDoctor ? "médico(a)" : "paciente";

    String subject = "Bem Vindo ao MedHealth - Credenciais de Acesso";
    String text = String.format("""
            Olá %s,
            
            Seu cadastro como %s foi realizado com sucesso no sistema MedHealth!
            
            📧 Login: %s
            🔑 Senha provisória: %s
  
            ⚠️ Por segurança, recomendamos alterar sua senha no primeiro acesso.
            
            Acesse a plaforma e realize o seu login.
            Após o login, vá em "Configurações" > "Alterar Senha"
            
            Atenciosamente,
            Equipe MedHealth
            """, person.getFullName(), role, person.getEmail(), tempPassword);

    EmailDto emailDto = new EmailDto(person.getEmail(), subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

  public void sendPasswordResetEmail(User user, String token) {
    String resetLink = "http://localhost:5173/reset-password?token=" + token;

    String subject = "MedHealth - Recuperação de Senha";
    String text = String.format("""
            Olá %s,
            
            Recebemos uma solicitação de recuperação de senha para sua conta.
            
            Clique no link abaixo para redefinir sua senha (válido por 1 hora):
            %s
            
            Se você não solicitou esta alteração, ignore este email.
            Sua senha permanecerá inalterada.
            
            Atenciosamente,
            Equipe MedHealth
            """, user.getPerson().getFullName(), resetLink);

    EmailDto emailDto = new EmailDto(user.getUsername(), subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

  public void sendAppointmentConfirmationToPatient(String patientEmail, AppointmentDto appointment) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta marcada com sucesso!";
    String text = String.format("""
            Olá %s,
            
            Sua consulta no MedHealth com o Dr(a). %s foi marcada para a data %s às %s.
            
            Caso não possa comparecer, pedimos a gentileza de avisar com antecedência.
            Ficamos à disposição para qualquer dúvida.
            
            Atenciosamente
            Equipe MedHealth
            """, appointment.patient().fullName(), appointment.doctor().fullName(), date, hour);

    EmailDto emailDto = new EmailDto(patientEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

  public void sendAppointmentConfirmationToDoctor(String doctorEmail, AppointmentDto appointment) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta marcada com sucesso!";
    String text = String.format("""
          Olá, Dr(a). %s
          
          Sua consulta no MedHealth com o paciente %s foi marcada para a data %s às %s
          
          Caso não possa comparecer, pedimos a gentileza de avisar com antecedência.
          Ficamos à disposição para qualquer dúvida.
          
          Atenciosamente,
          Equipe MedHealth
          """, appointment.doctor().fullName(), appointment.patient().fullName(), date, hour);

    EmailDto emailDto = new EmailDto(doctorEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }
  
  public void sendAppointmentCancelationToPatient(String patientEmail, AppointmentDto appointment, Cancellation cancellation) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta cancelada!";
    String text = String.format("""
          Olá, %s
          
          Sua consulta no MedHealth com o Dr(a). %s para a data %s às %s foi cancelada.
          
          Motivo: %s
          %s
          
          Ficamos à disposição para qualquer dúvida.
          Atenciosamente,
          Equipe MedHealth
          """,
        appointment.patient().fullName(),
        appointment.doctor().fullName(),
        date, hour,
        translateCancellationReason(cancellation.getReason()),
        cancellation.getMessage() != null && !cancellation.getMessage().isEmpty()
            ? "\n Mensagem: " + cancellation.getMessage()
            : "");

    EmailDto emailDto = new EmailDto(patientEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }
  
  public void sendAppointmentCancelationToDoctor(String doctorEmail, AppointmentDto appointment, Cancellation cancellation) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);
    
    String subject = "Consulta cancelada!";
    String text = String.format("""
          Olá, Dr(a). %s
          
          Sua consulta no MedHealth com o paciente %s para a data %s às %s foi cancelada.
          
          Motivo: %s
          %s
          
          Ficamos à disposição para qualquer dúvida.
          Atenciosamente,
          Equipe MedHealth
          """,
          appointment.doctor().fullName(),
          appointment.patient().fullName(),
          date, hour,
          translateCancellationReason(cancellation.getReason()),
          cancellation.getMessage() != null && !cancellation.getMessage().isEmpty()
            ? "\n Mensagem: " + cancellation.getMessage()
            : "");

    EmailDto emailDto = new EmailDto(doctorEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }
  
  public void sendAppointmentAttendanceToPatient(String patientEmail, AppointmentDto appointment) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta realizada com sucesso!";
    String text = String.format("""
          Olá, %s
          
          Sua consulta no MedHealth com o Dr(a). %s para a data %s às %s foi concluída!
          
          Agradecemos a preferência.
          Atenciosamente,
          Equipe MedHealth
          """, appointment.patient().fullName(), appointment.doctor().fullName(), date, hour);

    EmailDto emailDto = new EmailDto(patientEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

  public void sendAppointmentAttendanceToDoctor(String doctorEmail, AppointmentDto appointment) {
    LocalDateTime dateTime = appointment.date().toLocalDateTime();
    String date = dateTime.format(dateTimeFormatterDate);
    String hour = dateTime.format(dateTimeFormatterHour);

    String subject = "Consulta realizada com sucesso!";
    String text = String.format("""
          Olá, Dr(a). %s
          
          Sua consulta no MedHealth com o paciente %s para a data %s às %s foi concluída!
          
          Ficamos à disposição para qualquer dúvida.
          Atenciosamente,
          Equipe MedHealth
          """, appointment.doctor().fullName(), appointment.patient().fullName(), date, hour);
    EmailDto emailDto = new EmailDto(doctorEmail, subject, text);
    emailProducer.publishEmailMessage(emailDto);
  }

  private String translateCancellationReason(CancellationReason reason) {
    return switch (reason) {
      case PATIENT_CANCELED -> "Solicitado pelo paciente";
      case DOCTOR_CANCELED -> "Solicitado pelo médico";
      case PERSONAL_REASON -> "Paciente não compareceu";
      case MEDICAL_REASON -> "Médico não compareceu";
      case OTHER -> "Outros motivos";
    };
  }
}
