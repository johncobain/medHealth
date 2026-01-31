package br.edu.ifba.inf012.medHealthAPI.models.entities;

import br.edu.ifba.inf012.medHealthAPI.models.enums.DoctorStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "person_id", nullable = false, unique = true)
  private Person person;

  @Column(unique = true, nullable = false, length = 14)
  private String crm;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Specialty specialty;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private DoctorStatus status = DoctorStatus.ACTIVE;

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

  @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
  private List<Appointment> appointments;

  public Doctor() {}

  public Doctor(Person person, String crm, Specialty specialty) {
    this.person = person;
    this.crm = crm;
    this.specialty = specialty;
  }

  public Long getId() {return id;}
  public void setId(Long id) {this.id = id;}

  public Person getPerson() {return person;}
  public void setPerson(Person person) {this.person = person;}

  public String getCrm() {return crm;}
  public void setCrm(String crm) {this.crm = crm;}

  public Specialty getSpecialty() {return specialty;}
  public void setSpecialty(Specialty specialty) {this.specialty = specialty;}

  public DoctorStatus getStatus() {return status;}
  public void setStatus(DoctorStatus status) {this.status = status;}

  public Timestamp getCreatedAt() {return createdAt;}
  public Timestamp getUpdatedAt() {return updatedAt;}

  public List<Appointment> getAppointments() {return appointments;}
  public void setAppointments(List<Appointment> appointments) {this.appointments = appointments; }
}
