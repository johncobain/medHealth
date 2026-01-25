package br.edu.ifba.inf012.medHealthAPI.producers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.edu.ifba.inf012.medHealthAPI.dtos.email.EmailDto;

@Component
public class EmailProducer {
  @Value("${broker.queue.email-name}")
  private String routingKey;

  final RabbitTemplate rabbitTemplate;

  public EmailProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void publishEmailMessage(EmailDto emailDto) {
    rabbitTemplate.convertAndSend("",routingKey, emailDto);
  }
}
