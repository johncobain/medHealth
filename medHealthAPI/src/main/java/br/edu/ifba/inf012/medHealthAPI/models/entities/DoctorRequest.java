package br.edu.ifba.inf012.medHealthAPI.models.entities;

import java.sql.Timestamp;

import br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest.DoctorRequestDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest.DoctorRequestFormDto;
import br.edu.ifba.inf012.medHealthAPI.models.enums.DoctorRequestStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctor_request")
public class DoctorRequest {
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

    @Column(unique = true, nullable = false, length = 14)
    private String crm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialty specialty;

    @Column(name = "address_state", nullable = false)
    private String state;

    @Column(name = "address_city", nullable = false)
    private String city;

    @Column(name = "address_neighborhood", nullable = false)
    private String neighborhood;

    @Column(name = "address_street", nullable = false)
    private String street;

    @Column(name = "address_number")
    private String number;

    @Column(name = "address_complement")
    private String complement;

    @Column(name = "address_zip_code", nullable = false, length = 9)
    private String zipCode;

    @Enumerated(EnumType.STRING)
    private DoctorRequestStatus status;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        createdAt = now;
    }

    public DoctorRequest() {}

    public DoctorRequest(DoctorRequestFormDto dto){
        this.fullName = dto.fullName();
        this.email = dto.email();
        this.phone = dto.phone();
        this.cpf = dto.cpf();
        this.crm = dto.crm();
        this.specialty = dto.specialty();
        this.state = dto.state();
        this.city = dto.city();
        this.neighborhood = dto.neighborhood();
        this.street = dto.street();
        this.number = dto.number();
        this.complement = dto.complement();
        this.zipCode = dto.zipCode();
        this.status = dto.status();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCpf() {
        return cpf;
    }

    public String getCrm() {
        return crm;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getComplement() {
        return complement;
    }

    public String getZipCode() {
        return zipCode;
    }

    public DoctorRequestStatus getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
