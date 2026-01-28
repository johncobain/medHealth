package br.edu.ifba.inf012.medHealthAPI.services;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.cancelation.CancellationFormDto;
import br.edu.ifba.inf012.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Appointment;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Cancellation;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Patient;
import br.edu.ifba.inf012.medHealthAPI.models.enums.AppointmentStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.CancellationReason;
import br.edu.ifba.inf012.medHealthAPI.repositories.AppointmentRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.CancellationRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.DoctorRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.PatientRepository;
import br.edu.ifba.inf012.medHealthAPI.services.validations.AppointmentValidator;
import br.edu.ifba.inf012.medHealthAPI.services.validations.CancellationValidator;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
  private final AppointmentRepository appointmentRepository;
  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final CancellationRepository cancellationRepository;
  private final List<AppointmentValidator> validators;
  private final List<CancellationValidator> cancellationValidators;
  private final EmailService emailService;

  public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository, DoctorRepository doctorRepository, CancellationRepository cancellationRepository, List<AppointmentValidator> validators, List<CancellationValidator> cancellationValidators, EmailService emailService){
    this.appointmentRepository = appointmentRepository;
    this.patientRepository = patientRepository;
    this.doctorRepository = doctorRepository;
    this.cancellationRepository = cancellationRepository;
    this.validators = validators;
    this.cancellationValidators = cancellationValidators;
	  this.emailService = emailService;
  }

  public Page<AppointmentDto> getAll(Pageable pageable, Long doctorId, Long patientId, String status, Timestamp startDate, Timestamp endDate){
    Page<Appointment> appointments = appointmentRepository.findAllFiltered(pageable, doctorId, patientId, status, startDate, endDate);
    return appointments.map(AppointmentDto::new);
  }

  @Transactional
  public AppointmentDto schedule(AppointmentFormDto appointmentFormDto){
    validators.forEach(v -> v.validate(appointmentFormDto));

    Patient patient = patientRepository.findById(appointmentFormDto.patientId())
        .orElseThrow(() -> new EntityNotFoundException(Patient.class.getSimpleName(), appointmentFormDto.patientId()));

    Doctor doctor = chooseDoctor(appointmentFormDto);

    Appointment appointment = new Appointment();
    appointment.setPatient(patient);
    appointment.setDoctor(doctor);
    appointment.setDate(appointmentFormDto.date());
    appointment.setStatus(AppointmentStatus.SCHEDULED);

    AppointmentDto appointmentDtoSaved = AppointmentDto.fromEntity(appointmentRepository.save(appointment));

    emailService.sendAppointmentConfirmationToDoctor(doctor.getEmail(), appointmentDtoSaved);
    emailService.sendAppointmentConfirmationToPatient(patient.getEmail(), appointmentDtoSaved);

    return appointmentDtoSaved;
  }

  @Transactional
  public void cancel(Long appointmentId, CancellationFormDto cancellationFormDto){
    Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new EntityNotFoundException(Appointment.class.getSimpleName(), appointmentId));

    cancellationValidators.forEach(v -> v.validate(appointment));

    if(cancellationFormDto.reason() == CancellationReason.OTHER && (cancellationFormDto.message() == null || cancellationFormDto.message().isBlank())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message is required when reason is 'OUTROS'.");
    }

    appointment.setStatus(AppointmentStatus.CANCELED);
    appointmentRepository.save(appointment);

    Cancellation cancellation = new Cancellation(appointment, cancellationFormDto.reason(), cancellationFormDto.message());
    cancellationRepository.save(cancellation);

    AppointmentDto appointmentDto = AppointmentDto.fromEntity(appointment);

    emailService.sendAppointmentCancelationToDoctor(appointmentDto.doctor().email(), appointmentDto, cancellation);
    emailService.sendAppointmentCancelationToPatient(appointmentDto.patient().email(), appointmentDto, cancellation);
  }

  @Transactional
  public void attend(Long appointmentId){}// TODO: implement && email

  private Doctor chooseDoctor(AppointmentFormDto dto){
    if(dto.doctorId() != null){
      return doctorRepository.findById(dto.doctorId())
          .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), dto.doctorId()));
    }

    if(dto.specialty() == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Specialty is required if doctorId is not provided.");
    }

    List<Doctor> allDoctorsInSpecialty = doctorRepository.findAllActiveBySpecialty(dto.specialty().toUpperCase());

    if(allDoctorsInSpecialty.isEmpty()){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "No doctors available for this specialty.");
    }

    LocalDateTime start = dto.date().toLocalDateTime();
    Timestamp startTime = Timestamp.valueOf(start);
    Timestamp endTime = Timestamp.valueOf(start.plusHours(1));

    List<Appointment> appointmentsAtTime = appointmentRepository.findAppointmentsAtTheSameTime(startTime, endTime);
    List<Long> busyDoctorIds = appointmentsAtTime.stream().map(a -> a.getDoctor().getId()).toList();

    List<Doctor> availableDoctors = allDoctorsInSpecialty.stream()
        .filter(d -> !busyDoctorIds.contains(d.getId()))
        .toList();

    if(availableDoctors.isEmpty()){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "No doctors available at the selected time for this specialty.");
    }
    return availableDoctors.getFirst();
  }
}

