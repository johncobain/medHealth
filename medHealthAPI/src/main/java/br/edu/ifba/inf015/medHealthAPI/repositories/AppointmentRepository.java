package br.edu.ifba.inf015.medHealthAPI.repositories;

import br.edu.ifba.inf015.medHealthAPI.models.entities.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  boolean existsByDoctorIdAndDate(Long doctorId, Timestamp date);

  @Query("SELECT CASE WHEN COUNT(a) > 0 THEN TRUE ELSE FALSE END FROM Appointment a WHERE a.patient.id = :patientId AND FUNCTION('DATE', a.date) = FUNCTION('DATE', :date)")
  boolean existsByPatientIdAndDate(Long patientId, Timestamp date);

  @Query("SELECT a FROM Appointment a WHERE a.date BETWEEN :start AND :end")
  List<Appointment> findAppointmentsAtTheSameTime(Timestamp start, Timestamp end);

  @Query("SELECT a FROM Appointment a WHERE " +
    "(:doctorId IS NULL OR a.doctor.id = :doctorId) AND " +
    "(:patientId IS NULL OR a.patient.id = :patientId) AND " +
    "(:status IS NULL OR a.status = :status) AND " +
    "(CAST(:startDate AS timestamp) IS NULL OR a.date >= :startDate) AND " +
    "(CAST(:endDate AS timestamp) IS NULL OR a.date <= :endDate)")
  Page<Appointment> findAllFiltered(
    Pageable pageable,
    @Param("doctorId") Long doctorId,
    @Param("patientId") Long patientId,
    @Param("status") String status,
    @Param("startDate") Timestamp startDate,
    @Param("endDate") Timestamp endDate
  );
}
