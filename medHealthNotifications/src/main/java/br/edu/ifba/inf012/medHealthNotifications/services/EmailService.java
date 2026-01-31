package br.edu.ifba.inf012.medHealthNotifications.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.edu.ifba.inf012.medHealthNotifications.models.entities.Email;
import br.edu.ifba.inf012.medHealthNotifications.models.enums.EmailStatus;
import br.edu.ifba.inf012.medHealthNotifications.repositories.EmailRepository;
import jakarta.transaction.Transactional;

@Service
public class EmailService {
  final EmailRepository emailRepository;
  final JavaMailSender mailSender;

  public EmailService(EmailRepository emailRepository, JavaMailSender mailSender) {
    this.emailRepository = emailRepository;
    this.mailSender = mailSender;
  }

  @Value(value = "${spring.mail.username}")
  private String mailFrom;

  @Transactional
  public Email sendEmail(Email email){
    if (email.getMailTo().equals(mailFrom)) {
      System.out.println("Tentativa de enviar email para o mesmo endereço: " + email.getMailTo());
      email.setStatus(EmailStatus.ERROR);
      return emailRepository.save(email);
    }
    try{
      email.setMailFrom(mailFrom);

      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(email.getMailFrom());
      message.setTo(email.getMailTo());
      message.setSubject(email.getSubject());
      message.setText(email.getText());

      mailSender.send(message);
      email.setStatus(EmailStatus.SENT);
    }catch (MailException e){
      email.setStatus(EmailStatus.ERROR);
    }
    return emailRepository.save(email);
  }

}
