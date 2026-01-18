package br.edu.ifba.inf015.medHealthAPI.models.entities;

import br.edu.ifba.inf015.medHealthAPI.dtos.address.AddressFormDto;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String state;
    private String city;
    private String neighborhood;
    private String street;
    private String number;
    private String complement;
    private String zipCode;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    protected Address() { }

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

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
