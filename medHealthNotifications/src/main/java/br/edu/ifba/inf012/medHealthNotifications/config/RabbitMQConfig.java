package br.edu.ifba.inf012.medHealthNotifications.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RabbitMQConfig {
  @Value("${broker.queue.email-name}")
  private String queueName;

  private String getDelayQueueName() { return queueName + ".delay"; }
  private String getExchangeName() { return queueName + ".exchange"; }
  private String getRoutingKey() { return queueName + ".key"; }

  @Bean
  public Queue queue(){
    return new Queue(queueName, true);
  }

  @Bean
  public Queue delayQueue(){
    return QueueBuilder.durable(getDelayQueueName())
        .withArgument("x-dead-letter-exchange", getExchangeName())
        .withArgument("x-dead-letter-routing-key", getRoutingKey())
        .withArgument("x-message-ttl", 60000) 
        .build();
  }

  @Bean
  public DirectExchange directExchange() {
    return new DirectExchange(getExchangeName());
  }

  @Bean
  public Binding binding() {
    return BindingBuilder.bind(queue()).to(directExchange()).with(getRoutingKey());
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter(){
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return new Jackson2JsonMessageConverter(objectMapper);
  }
}
