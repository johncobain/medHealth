package br.edu.ifba.inf012.medHealthAPI.controllers;

import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
public class PatientController {
  private final PatientService patientService;

  public PatientController(PatientService patientService){
      this.patientService = patientService;
  }

  @GetMapping
  @Operation(summary = "Retorna todos os pacientes")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<Page<PatientDto>> findAll(
          @ParameterObject
          @PageableDefault(size = 10, sort = {"person.fullName"}, direction = Sort.Direction.ASC)
          Pageable pageable,
          @RequestParam(required = false) String name
  ){
    return ResponseEntity.ok(patientService.findAll(pageable, name));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Retorna um paciente")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PatientDto> findById(@PathVariable Long id) {
    return ResponseEntity.ok(patientService.findById(id));
  }

  @GetMapping("/email/{email}")
  @Operation(summary = "Retorna um paciente por email")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PatientDto> findByEmail(@PathVariable String email) {
    return ResponseEntity.ok(patientService.findByEmail(email));
  }

  @GetMapping("/cpf/{cpf}")
  @Operation(summary = "Retorna um paciente por cpf")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PatientDto> findByCpf(@PathVariable String cpf) {
    return ResponseEntity.ok(patientService.findByCpf(cpf));
  }

  @PostMapping
  @Operation(summary = "Create a new Patient")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<PatientDto> create(@Valid @RequestBody PatientFormDto dto) {
    PatientDto patient = patientService.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(patient);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualiza um paciente")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PatientDto> update(@PathVariable Long id, @Valid @RequestBody PatientUpdateDto patient){
    return ResponseEntity.ok(patientService.update(id, patient));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Deleta um paciente (soft delete)")
  @ApiResponse(responseCode = "204")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    patientService.deactivate(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  @Operation(summary = "Retorna dados do paciente autenticado")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PatientDto> getMyData(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(patientService.findByEmail(user.getUsername()));
  }

  @PutMapping("/me")
  @Operation(summary = "Atualiza dados do paciente autenticado")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PatientDto> updateMyData(
      @AuthenticationPrincipal User user,
      @Valid @RequestBody PatientUpdateDto dto
  ) {
    PatientDto patient = patientService.findByEmail(user.getUsername());
    return ResponseEntity.ok(patientService.update(patient.id(), dto));
  }
}
