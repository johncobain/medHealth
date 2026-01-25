package br.edu.ifba.inf012.medHealthNotifications.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.edu.ifba.inf012.medHealthNotifications.dtos.EmailDto;
import br.edu.ifba.inf012.medHealthNotifications.models.entities.Email;
import br.edu.ifba.inf012.medHealthNotifications.services.EmailService;

@Component
public class EmailConsumer {
  private final EmailService emailService;

  public EmailConsumer(EmailService emailService) {
    this.emailService = emailService;
  }

  @RabbitListener(queues = "${broker.queue.email-name}")
  public void listenEmailQueue(@Payload EmailDto emailDto){
    Email email = new Email(emailDto);
    emailService.sendEmail(email);
  }
}
