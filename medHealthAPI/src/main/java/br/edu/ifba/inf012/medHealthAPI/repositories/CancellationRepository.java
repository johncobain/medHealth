package br.edu.ifba.inf012.medHealthAPI.repositories;

import br.edu.ifba.inf012.medHealthAPI.models.entities.Cancellation;
import br.edu.ifba.inf012.medHealthAPI.models.enums.CancellationReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CancellationRepository extends JpaRepository<Cancellation, Long> {
  Optional<Cancellation> findByAppointmentId(Long appointmentId);

  boolean existsByAppointmentId(Long appointmentId);

  List<Cancellation> findByReason(CancellationReason reason);

  @Query("SELECT c FROM Cancellation c WHERE c.appointment.patient.id = :patientId")
  List<Cancellation> findByPatientId(Long patientId);

  @Query("SELECT c FROM Cancellation c WHERE c.appointment.doctor.id = :doctorId")
  List<Cancellation> findByDoctorId(Long doctorId);
}
