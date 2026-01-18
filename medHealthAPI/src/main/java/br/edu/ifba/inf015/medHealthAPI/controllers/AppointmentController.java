package br.edu.ifba.inf015.medHealthAPI.controllers;

import br.edu.ifba.inf015.medHealthAPI.dtos.appointment.AppointmentDto;
import br.edu.ifba.inf015.medHealthAPI.dtos.appointment.AppointmentFormDto;
import br.edu.ifba.inf015.medHealthAPI.dtos.cancelation.CancellationFormDto;
import br.edu.ifba.inf015.medHealthAPI.services.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
  private final AppointmentService appointmentService;

  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  @GetMapping
  @Operation(summary = "Get all appointments with filters")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<Page<AppointmentDto>> getAll(
    @ParameterObject @PageableDefault(size = 10, sort = {"date"}, direction = Sort.Direction.ASC) Pageable pageable,
    @RequestParam(required = false) Long doctorId,
    @RequestParam(required = false) Long patientId,
    @RequestParam(required = false) String status,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
  ){
    Timestamp startTimestamp = (startDate != null) ? Timestamp.valueOf(startDate) : null;
    Timestamp endTimestamp = (endDate != null) ? Timestamp.valueOf(endDate) : null;

    Page<AppointmentDto> appointments = appointmentService.getAll(pageable, doctorId, patientId, status, startTimestamp, endTimestamp);
    return ResponseEntity.ok(appointments);
  }

  @PostMapping
  @Operation(summary = "Schedule a new appointment")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<AppointmentDto> create(@Valid @RequestBody AppointmentFormDto appointmentFormDto) {
    AppointmentDto appointment = appointmentService.schedule(appointmentFormDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
  }

  @PostMapping("/{appointmentId}/cancel")
  @Operation(summary = "Cancel an appointment")
  @ApiResponse(responseCode = "204")
  public ResponseEntity<Void> cancel(@PathVariable Long appointmentId, @Valid @RequestBody CancellationFormDto cancellationFormDto){
    appointmentService.cancel(appointmentId, cancellationFormDto);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
