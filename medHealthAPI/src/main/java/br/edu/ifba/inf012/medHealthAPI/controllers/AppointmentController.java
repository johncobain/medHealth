package br.edu.ifba.inf012.medHealthAPI.controllers;

import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.appointment.AppointmentFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.cancelation.CancellationFormDto;
import br.edu.ifba.inf012.medHealthAPI.services.AppointmentService;
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
  @Operation(summary = "Retorna todos os agendamentos")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<Page<AppointmentDto>> findAll(
    @ParameterObject @PageableDefault(size = 10, sort = {"date"}, direction = Sort.Direction.ASC) Pageable pageable,
    @RequestParam(required = false) Long doctorId,
    @RequestParam(required = false) Long patientId,
    @RequestParam(required = false) String status,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
  ){
    Timestamp startTimestamp = (startDate != null) ? Timestamp.valueOf(startDate) : null;
    Timestamp endTimestamp = (endDate != null) ? Timestamp.valueOf(endDate) : null;

    Page<AppointmentDto> appointments = appointmentService.findAll(pageable, doctorId, patientId, status, startTimestamp, endTimestamp);
    return ResponseEntity.ok(appointments);
  }

  @GetMapping("/{id}")
  public ResponseEntity<AppointmentDto> findById(@PathVariable Long id) {
    return ResponseEntity.ok(appointmentService.findById(id));
  }

  @PostMapping
  @Operation(summary = "Agenda um novo agendamento")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<AppointmentDto> create(@Valid @RequestBody AppointmentFormDto dto) {
    AppointmentDto appointment = appointmentService.schedule(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
  }

  @PostMapping("/{id}/cancel")
  @Operation(summary = "Cancela um agendamento")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<AppointmentDto> cancel(@PathVariable Long id, @Valid @RequestBody CancellationFormDto dto){
    return ResponseEntity.ok(appointmentService.cancel(id, dto));
  }

  @PatchMapping("/{id}/attend")
  @Operation(summary = "Marca um agendamento como realizado")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<AppointmentDto> attend(@PathVariable Long id){
    return ResponseEntity.ok(appointmentService.attend(id));
  }
}
