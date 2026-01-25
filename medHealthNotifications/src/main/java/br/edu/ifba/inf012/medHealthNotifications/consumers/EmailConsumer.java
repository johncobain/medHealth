package br.edu.ifba.inf012.medHealthNotifications.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.edu.ifba.inf012.medHealthNotifications.dtos.EmailDto;

@Component
public class EmailConsumer {
  @RabbitListener(queues = "${broker.queue.email-name}")
  public void listenEmailQueue(@Payload EmailDto message){
    System.out.println("Received Email Notification: " + message);
  }
}
