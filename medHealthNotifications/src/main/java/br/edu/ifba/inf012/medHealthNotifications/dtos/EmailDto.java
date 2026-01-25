package br.edu.ifba.inf012.medHealthNotifications.dtos;

import br.edu.ifba.inf012.medHealthNotifications.models.entities.Email;

public record EmailDto(String mailFrom, String mailTo, String subject, String text) {
  public EmailDto(Email email) {
    this(email.getMailFrom(), email.getMailTo(), email.getSubject(), email.getText());
  }
}
