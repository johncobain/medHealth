package br.edu.ifba.inf012.medHealthAPI.controllers;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.cancellation.CancellationFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.cancellation.CancellationReasonDto;
import br.edu.ifba.inf012.medHealthAPI.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
  private final AppointmentService appointmentService;

  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  @GetMapping
  @Operation(summary = "Retorna todos os agendamentos")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<Page<AppointmentDto>> findAll(
    @ParameterObject @PageableDefault(size = 10, sort = {"date"}, direction = Sort.Direction.ASC) Pageable pageable,
    @RequestParam(required = false) Long doctorId,
    @RequestParam(required = false) Long patientId,
    @RequestParam(required = false) String status,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
    Authentication authentication
  ){
    if (authentication != null) {
      boolean isAdmin = authentication.getAuthorities().stream()
              .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
      boolean isDoctor = authentication.getAuthorities().stream()
              .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));
      boolean isPatient = authentication.getAuthorities().stream()
              .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));
      
      String email = authentication.getName();

      if (!isAdmin) {
        if (isDoctor) {
          doctorId = appointmentService.getDoctorIdByEmail(email);
        } else if (isPatient) {
          patientId = appointmentService.getPatientIdByEmail(email);
        }
      }
    }
    Timestamp startTimestamp = (startDate != null) ? Timestamp.valueOf(startDate) : null;
    Timestamp endTimestamp = (endDate != null) ? Timestamp.valueOf(endDate) : null;

    Page<AppointmentDto> appointments = appointmentService.findAll(pageable, doctorId, patientId, status, startTimestamp, endTimestamp);
    return ResponseEntity.ok(appointments);
  }

  @GetMapping("/recent")
  @Operation(summary = "Retorna os cinco agendamentos mais recentes")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<Page<AppointmentDto>> getRecent(Authentication authentication) {
    Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "date"));
    
    Long doctorId = null;
    Long patientId = null;
    if (authentication != null) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_DOCTOR"));
        boolean isPatient = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_PATIENT"));
        
        String email = authentication.getName();

        if (!isAdmin) {
            if (isDoctor) {
                doctorId = appointmentService.getDoctorIdByEmail(email);
            } else if (isPatient) {
                patientId = appointmentService.getPatientIdByEmail(email);
            }
        }
    }
    
    return ResponseEntity.ok(appointmentService.findAll(pageable, doctorId, patientId, "SCHEDULED", null, null));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AppointmentDto> findById(@PathVariable Long id) {
    return ResponseEntity.ok(appointmentService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Agenda uma nova consulta")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<AppointmentDto> create(@Valid @RequestBody AppointmentFormDto dto) {
    AppointmentDto appointment = appointmentService.schedule(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
  }

  @PatchMapping("/{id}/cancel")
  @Operation(summary = "Cancela uma consulta")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<AppointmentDto> cancel(@PathVariable Long id, @Valid @RequestBody CancellationFormDto dto){
    return ResponseEntity.ok(appointmentService.cancel(id, dto));
  }

  @PatchMapping("/{id}/complete")
  @Operation(summary = "Marca uma consulta como realizada")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<AppointmentDto> attend(@PathVariable Long id){
    return ResponseEntity.ok(appointmentService.attend(id));
  }

  @GetMapping("/getCancellationReasons")
  @Operation(summary = "Retorna os motivos de cancelamento")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<List<CancellationReasonDto>> getCancellationReasons(){
    return ResponseEntity.ok(appointmentService.getCancellationReasons());
  }

}
