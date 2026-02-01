package br.edu.ifba.inf012.medHealthAPI.services;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.cancelation.CancellationFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.cancelation.CancellationReasonDto;
import br.edu.ifba.inf012.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Appointment;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Cancellation;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Patient;
import br.edu.ifba.inf012.medHealthAPI.models.enums.AppointmentStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.CancellationReason;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;
import br.edu.ifba.inf012.medHealthAPI.repositories.AppointmentRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.CancellationRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.DoctorRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.PatientRepository;
import br.edu.ifba.inf012.medHealthAPI.services.validations.AppointmentValidator;
import br.edu.ifba.inf012.medHealthAPI.services.validations.CancellationValidator;
import jakarta.transaction.Transactional;

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

  public Page<AppointmentDto> findAll(Pageable pageable, Long doctorId, Long patientId, String status, Timestamp startDate, Timestamp endDate){
    AppointmentStatus statusEnum = null;
    if(status != null && !status.isEmpty()){
      try{
        statusEnum = AppointmentStatus.valueOf(status.toUpperCase());
      }catch(IllegalArgumentException e){
      }
    }
      Page<Appointment> appointments = appointmentRepository.findAllFiltered(pageable, doctorId, patientId, statusEnum, startDate, endDate);
    return appointments.map(AppointmentDto::fromEntity);
  }

  public AppointmentDto findById(Long id) {
    Appointment appointment = appointmentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Appointment.class.getSimpleName(), id));
    return AppointmentDto.fromEntity(appointment);
  }

  @Transactional
  public AppointmentDto schedule(AppointmentFormDto dto){
    validators.forEach(v -> v.validate(dto));

    Patient patient = patientRepository.findById(dto.patientId())
        .orElseThrow(() -> new EntityNotFoundException(Patient.class.getSimpleName(), dto.patientId()));

    Doctor doctor = chooseDoctor(dto);

    Appointment appointment = new Appointment();
    appointment.setPatient(patient);
    appointment.setDoctor(doctor);
    appointment.setDate(dto.date());
    appointment.setStatus(AppointmentStatus.SCHEDULED);
    appointment = appointmentRepository.save(appointment);

    AppointmentDto appointmentDtoSaved = AppointmentDto.fromEntity(appointment);

    emailService.sendAppointmentConfirmationToDoctor(doctor.getPerson().getEmail(), appointmentDtoSaved);
    emailService.sendAppointmentConfirmationToPatient(patient.getPerson().getEmail(), appointmentDtoSaved);

    return appointmentDtoSaved;
  }

  @Transactional
  public AppointmentDto cancel(Long id, CancellationFormDto dto){
    Appointment appointment = appointmentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Appointment.class.getSimpleName(), id));

    Appointment finalAppointment = appointment;
    cancellationValidators.forEach(v -> v.validate(finalAppointment));

    if(dto.reason() == CancellationReason.OTHER && (dto.message() == null || dto.message().isBlank())){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Motivo de cancelamento é obrigatório quando a razão for 'OUTROS'.");
    }

    appointment.setStatus(AppointmentStatus.CANCELED);
    appointment = appointmentRepository.save(appointment);

    Cancellation cancellation = new Cancellation(appointment, dto.reason(), dto.message());
    cancellationRepository.save(cancellation);

    AppointmentDto appointmentDto = AppointmentDto.fromEntity(appointment);

    emailService.sendAppointmentCancelationToDoctor(appointmentDto.doctor().email(), appointmentDto, cancellation);
    emailService.sendAppointmentCancelationToPatient(appointmentDto.patient().email(), appointmentDto, cancellation);

    return appointmentDto;
  }

  @Transactional
  public AppointmentDto attend(Long appointmentId){
    Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new EntityNotFoundException(Appointment.class.getSimpleName(), appointmentId));

    if(appointment.getStatus() == AppointmentStatus.CANCELED){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível completar uma consulta cancelada");
    }

    if (appointment.getStatus() == AppointmentStatus.ATTENDED) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Consulta já foi concluída");
    }

    Timestamp now = new Timestamp(System.currentTimeMillis());
    if (now.before(appointment.getDate())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possível concluir uma consulta que ainda não aconteceu");
    }
    appointment.setStatus(AppointmentStatus.ATTENDED);
    appointment = appointmentRepository.save(appointment);

    AppointmentDto appointmentDto = AppointmentDto.fromEntity(appointment);

    emailService.sendAppointmentAttendanceToDoctor(appointmentDto.doctor().email(), appointmentDto);
    emailService.sendAppointmentAttendanceToPatient(appointmentDto.patient().email(), appointmentDto);

    return appointmentDto;
  }

  private Doctor chooseDoctor(AppointmentFormDto dto){
    if(dto.doctorId() != null){
      return doctorRepository.findById(dto.doctorId())
          .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), dto.doctorId()));
    }

    if(dto.specialty() == null){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Especialidade é obrigatória se o id do médico não for fornecido.");
    }

    List<Doctor> allDoctorsInSpecialty = doctorRepository.findAllActiveBySpecialty(Specialty.valueOf(dto.specialty()));

    if(allDoctorsInSpecialty.isEmpty()){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Nenhum médico disponível para essa especialidade.");
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
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Nenhum médico disponível para essa especialidade.");
    }
    return availableDoctors.getFirst();
  }

  public long countTotal() {
      return appointmentRepository.count();
  }

  public long countScheduled() {
      return appointmentRepository.countByStatus(AppointmentStatus.SCHEDULED);
  }

  public long countToday() {
      LocalDateTime now = LocalDateTime.now();
      Timestamp start = Timestamp.valueOf(now.with(LocalTime.MIN));
      Timestamp end = Timestamp.valueOf(now.with(LocalTime.MAX));
      return appointmentRepository.countByDateBetween(start, end);
  }

  public Long getDoctorIdByEmail(String email) {
    return doctorRepository.findByPersonEmail(email)
            .map(Doctor::getId)
            .orElse(null);
  }

  public Long getPatientIdByEmail(String email) {
    return patientRepository.findByPersonEmail(email)
            .map(Patient::getId)
            .orElse(null);
  }

  public List<CancellationReasonDto> getCancellationReasons() {
    return Arrays.stream(CancellationReason.values())
      .map(s -> new CancellationReasonDto(
        s.name(),
        s.getDescription()
      )).toList();
  }
}

