package br.edu.ifba.inf012.medHealthAPI.models.entities;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressFormDto;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String state;

  @Column(nullable = false)
  private String city;

  @Column(nullable = false)
  private String neighborhood;

  @Column(nullable = false)
  private String street;

  private String number;

  private String complement;

  @Column(name = "zip_code", nullable = false, length = 9)
  private String zipCode;

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

  protected Address() { }

  public Address(AddressDto addressDto){
      this.state = addressDto.state();
      this.city = addressDto.city();
      this.neighborhood = addressDto.neighborhood();
      this.street = addressDto.street();
      this.number = addressDto.number();
      this.complement = addressDto.complement();
      this.zipCode = addressDto.zipCode();
  }

  public Address(AddressFormDto addressFormDto){
      this.state = addressFormDto.state();
      this.city = addressFormDto.city();
      this.neighborhood = addressFormDto.neighborhood();
      this.street = addressFormDto.street();
      this.number = addressFormDto.number();
      this.complement = addressFormDto.complement();
      this.zipCode = addressFormDto.zipCode();
  }

  public Long getId() {
      return id;
  }
  public void setId(Long id) {
      this.id = id;
  }

  public String getState() {
      return state;
  }
  public void setState(String state) {
      this.state = state;
  }

  public String getCity() {
      return city;
  }
  public void setCity(String city) {
      this.city = city;
  }

  public String getNeighborhood() {
      return neighborhood;
  }
  public void setNeighborhood(String neighborhood) {
      this.neighborhood = neighborhood;
  }

  public String getStreet() {
      return street;
  }
  public void setStreet(String street) {
      this.street = street;
  }

  public String getNumber() {
      return number;
  }
  public void setNumber(String number) {
      this.number = number;
  }

  public String getComplement() {
      return complement;
  }
  public void setComplement(String complement) {
      this.complement = complement;
  }

  public Timestamp getCreatedAt() {
      return createdAt;
  }
  public Timestamp getUpdatedAt() {
      return updatedAt;
  }

  public String getZipCode() {
      return zipCode;
  }
  public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
