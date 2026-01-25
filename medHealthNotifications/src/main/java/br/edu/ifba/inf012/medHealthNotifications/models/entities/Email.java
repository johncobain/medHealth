package br.edu.ifba.inf012.medHealthNotifications.models.entities;

import java.time.LocalDateTime;

import br.edu.ifba.inf012.medHealthNotifications.dtos.EmailDto;
import br.edu.ifba.inf012.medHealthNotifications.models.enums.EmailStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "emails")
public class Email {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String mailFrom;
  private String mailTo;
  private String subject;
  private String text;
  private LocalDateTime sentAt;
  @Enumerated(EnumType.STRING)
  private EmailStatus status = EmailStatus.SENT;

  public Email(){}

  public Email(EmailDto dto){
    this.mailFrom = dto.mailFrom();
    this.mailTo = dto.mailTo();
    this.subject = dto.subject();
    this.text = dto.text();
    this.sentAt = LocalDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMailFrom() {
    return mailFrom;
  }

  public void setMailFrom(String mailFrom) {
    this.mailFrom = mailFrom;
  }

  public String getMailTo() {
    return mailTo;
  }

  public void setMailTo(String mailTo) {
    this.mailTo = mailTo;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
  
  public LocalDateTime getSentAt() {
    return sentAt;
  }

  public void setSentAt(LocalDateTime sentAt) {
    this.sentAt = sentAt;
  }
  public EmailStatus getStatus() {
    return status;
  }

  public void setStatus(EmailStatus status) {
    this.status = status;
  }
}