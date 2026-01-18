package br.edu.ifba.inf015.medHealthAPI.models.entities;

import br.edu.ifba.inf015.medHealthAPI.dtos.doctor.DoctorFormDto;
import br.edu.ifba.inf015.medHealthAPI.models.enums.Specialty;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;
    @Column(unique = true)
    private String crm;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    private String status;

    private Timestamp createdAt;
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    public Doctor() {}

    public Doctor(DoctorFormDto doctorFormDto) {
        this.name = doctorFormDto.name();
        this.email = doctorFormDto.email();
        this.phone = doctorFormDto.phone();
        this.crm = doctorFormDto.crm();
        this.address = new Address(doctorFormDto.address());
        this.specialty = Specialty.valueOf(doctorFormDto.specialty().toUpperCase());
        this.status = "ACTIVE";
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
