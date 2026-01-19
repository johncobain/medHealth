package br.edu.ifba.inf012.medHealthAPI.controllers;

import br.edu.ifba.inf012.medHealthAPI.dtos.patient.PatientDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.patient.PatientFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.patient.PatientUpdateDto;
import br.edu.ifba.inf012.medHealthAPI.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {
  private final PatientService patientService;

  public PatientController(PatientService patientService){
      this.patientService = patientService;
  }

  @GetMapping
  @Operation(summary = "Get All active Patients")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<Page<PatientDto>> getAll(
          @ParameterObject
          @PageableDefault(size = 10, sort = {"name"}, direction = Sort.Direction.ASC)
          Pageable pageable
  ){
    return ResponseEntity.ok(patientService.getAll(pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Return an Active Patient")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PatientDto> getOne(@PathVariable Long id){
    return ResponseEntity.ok(patientService.getOne(id));
  }

  @PostMapping
  @Operation(summary = "Create a new Patient")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<PatientDto> create(@Valid @RequestBody PatientFormDto patient) {
    return ResponseEntity.status(HttpStatus.CREATED).body(patientService.save(patient));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an Active Patient")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PatientDto> update(@Valid @RequestBody PatientUpdateDto patient, @PathVariable Long id){
    return ResponseEntity.ok(patientService.update(patient, id));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an Active Patient")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PatientDto> delete(@PathVariable Long id){
    return ResponseEntity.ok(patientService.delete(id));
  }
}
