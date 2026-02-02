package br.edu.ifba.inf012.medHealthNotifications.consumers;

import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.edu.ifba.inf012.medHealthNotifications.dtos.EmailDto;
import br.edu.ifba.inf012.medHealthNotifications.models.entities.Email;
import br.edu.ifba.inf012.medHealthNotifications.models.enums.EmailStatus;
import br.edu.ifba.inf012.medHealthNotifications.services.EmailService;

@Component
public class EmailConsumer {
  private final EmailService emailService;
  private final RabbitTemplate rabbitTemplate;

  @Value("${broker.queue.email-name}")
  private String queueName;

  public EmailConsumer(EmailService emailService, RabbitTemplate rabbitTemplate) {
    this.emailService = emailService;
    this.rabbitTemplate = rabbitTemplate;
  }

  @RabbitListener(queues = "${broker.queue.email-name}")
  public void listenEmailQueue(@Payload EmailDto emailDto, @Header(required = false, name = "x-death") List<Map<String, ?>> xDeath){
    Email email = new Email(emailDto);
    Email processedEmail = emailService.sendEmail(email);

    if(processedEmail.getStatus() == EmailStatus.ERROR) {
       long retryCount = getRetryCount(xDeath);
       
       if (retryCount < 3) {
           System.out.println("Erro ao enviar email. Reenviando para delay. Tentativa: " + (retryCount + 1));
           rabbitTemplate.convertAndSend(queueName + ".delay", emailDto);
       } else {
           System.err.println("Máximo de tentativas atingido para o email: " + email.getMailTo());
       }
    }
  }

  private long getRetryCount(List<Map<String, ?>> xDeath) {
    if (xDeath != null && !xDeath.isEmpty()) {
        Map<String, ?> entry = xDeath.get(0);
        Long count = (Long) entry.get("count");
        return count != null ? count : 0;
    }
    return 0;
  }
}
