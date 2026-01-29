package br.edu.ifba.inf012.medHealthAPI.models.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import br.edu.ifba.inf012.medHealthAPI.models.enums.AppointmentStatus;

@Entity
@Table(name = "appointments")
public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Timestamp date;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doctor_id")
  private Doctor doctor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patient_id")
  private Patient patient;

  @Enumerated(EnumType.STRING)
  private AppointmentStatus status;

  @Column(name = "created_at", updatable = false)
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @PrePersist
  protected void onCreate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    updatedAt = now;
  }

  public Appointment() {}

  public Appointment(Timestamp date, Doctor doctor, Patient patient, AppointmentStatus status) {
    this.date = date;
    this.doctor = doctor;
    this.patient = patient;
    this.status = status;
  }

  public Long getId() {
      return id;
  }
  public void setId(Long id) {
      this.id = id;
  }

  public Timestamp getDate() {
      return date;
  }
  public void setDate(Timestamp date) {
      this.date = date;
  }

  public Doctor getDoctor() {
      return doctor;
  }
  public void setDoctor(Doctor doctor) {
      this.doctor = doctor;
  }

  public Patient getPatient() {
      return patient;
  }
  public void setPatient(Patient patient) {
      this.patient = patient;
  }

  public AppointmentStatus getStatus() {
      return status;
  }
  public void setStatus(AppointmentStatus status) {
      this.status = status;
  }

  public Timestamp getCreatedAt() {
      return createdAt;
  }
  public Timestamp getUpdatedAt() {
      return updatedAt;
  }}
