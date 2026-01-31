package br.edu.ifba.inf012.medHealthAPI.models.entities;

import br.edu.ifba.inf012.medHealthAPI.models.enums.PersonStatus;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "persons")
public class Person{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false, length = 18)
  private String phone;

  @Column(unique = true, nullable = false, length = 14)
  private String cpf;

  @ManyToOne
  @JoinColumn(name = "address_id", nullable = false)
  private Address address;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private PersonStatus status = PersonStatus.ACTIVE;

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

  public Person() {}

  public Person(String fullName, String email, String phone, String cpf, Address address) {
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.cpf = cpf;
    this.address = address;
  }

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getFullName() { return fullName; }
  public void setFullName(String fullName) { this.fullName = fullName; }

  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public String getCpf() { return cpf; }
  public void setCpf(String cpf) { this.cpf = cpf; }

  public Address getAddress() { return address; }
  public void setAddress(Address address) { this.address = address; }

  public PersonStatus getStatus() { return status; }
  public void setStatus(PersonStatus status) { this.status = status; }

  public Timestamp getCreatedAt() { return createdAt; }
  public Timestamp getUpdatedAt() { return updatedAt; }
}