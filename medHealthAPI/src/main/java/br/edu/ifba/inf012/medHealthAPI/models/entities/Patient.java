package br.edu.ifba.inf012.medHealthAPI.models.entities;

import br.edu.ifba.inf012.medHealthAPI.models.enums.PatientStatus;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "person_id", nullable = false, unique = true)
  private Person person;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private PatientStatus status = PatientStatus.ACTIVE;

  @Column(name = "created_at", updatable = false)
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
  private List<Appointment> appointments;

  @PrePersist
  protected void onCreate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    this.updatedAt = now;
  }

  public Patient() {
  }

  public Patient(Person person) {
    this.person = person;
  }

  public Long getId() {return id;}
  public void setId(Long id) {this.id = id;}

  public Person getPerson() {return person;}
  public void setPerson(Person person) {this.person = person;}

  public PatientStatus getStatus() {return status;}
  public void setStatus(PatientStatus status) {this.status = status;}

  public Timestamp getCreatedAt() {return createdAt;}
  public Timestamp getUpdatedAt() {return updatedAt;}

  public List<Appointment> getAppointments() {return appointments;}
  public void setAppointments(List<Appointment> appointments) {this.appointments = appointments;}
}
