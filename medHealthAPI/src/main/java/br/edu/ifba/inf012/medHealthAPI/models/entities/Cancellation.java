package br.edu.ifba.inf012.medHealthAPI.models.entities;

import br.edu.ifba.inf012.medHealthAPI.models.enums.CancellationReason;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "cancellations")
public class Cancellation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "appointment_id")
  private Appointment appointment;

  @Enumerated(EnumType.STRING)
  private CancellationReason reason;

  private String message;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  public Cancellation(){}

  public Cancellation(Appointment appointment, CancellationReason reason, String message){
    this.appointment = appointment;
    this.reason = reason;
    this.message = message;
  }

  @PrePersist
  protected void onCreate(){
    Timestamp now = new Timestamp(System.currentTimeMillis());
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate(){
    this.updatedAt = new Timestamp(System.currentTimeMillis());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Appointment getAppointment() {
    return appointment;
  }

  public void setAppointment(Appointment appointment) {
    this.appointment = appointment;
  }

  public CancellationReason getReason() {
    return reason;
  }

  public void setReason(CancellationReason reason) {
    this.reason = reason;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }
}
