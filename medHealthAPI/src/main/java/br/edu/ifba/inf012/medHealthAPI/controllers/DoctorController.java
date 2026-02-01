package br.edu.ifba.inf012.medHealthAPI.controllers;

import br.edu.ifba.inf012.medHealthAPI.models.entities.User;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorUpdateDto;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;
import br.edu.ifba.inf012.medHealthAPI.services.DoctorService;
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
@RequestMapping("/doctors")
public class DoctorController {
  private final DoctorService doctorService;

  public DoctorController(DoctorService doctorService){
    this.doctorService = doctorService;
  }

  @GetMapping
  @Operation(summary = "Retorna todos os médicos")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<Page<DoctorDto>> findAll(
    @ParameterObject
    @PageableDefault(size = 10, sort = {"fullName"}, direction = Sort.Direction.ASC)
    Pageable pageable
  ){
    return ResponseEntity.ok(doctorService.findAll(pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Retorna um médico")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> findById(@PathVariable Long id){
    return ResponseEntity.ok(doctorService.findById(id));
  }

  @GetMapping("/email/{email}")
  @Operation(summary = "Retorna um médico por email")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> findByEmail(@PathVariable String email) {
    return ResponseEntity.ok(doctorService.findByEmail(email));
  }

  @GetMapping("/cpf/{cpf}")
  @Operation(summary = "Retorna um médico por cpf")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> findByCpf(@PathVariable String cpf) {
    return ResponseEntity.ok(doctorService.findByCpf(cpf));
  }

  @GetMapping("/crm/{crm}")
  @Operation(summary = "Retorna um médico por crm")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> findByCrm(@PathVariable String crm) {
    return ResponseEntity.ok(doctorService.findByCrm(crm));
  }

  @GetMapping("/specialty/{specialty}")
  @Operation(summary = "Retorna médicos por especialidade")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<Page<DoctorDto>> findBySpecialty(
      @PathVariable Specialty specialty,
      @ParameterObject
      @PageableDefault(size = 10, sort = {"fullName"}, direction = Sort.Direction.ASC)
      Pageable pageable
  ) {
    return ResponseEntity.ok(doctorService.findBySpecialty(specialty, pageable));
  }

  @PostMapping
  @Operation(summary = "Cria um novo médico")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<DoctorDto> create(@Valid @RequestBody DoctorFormDto dto) {
    DoctorDto doctor = doctorService.save(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(doctor);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Atualiza um médico")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> update(@PathVariable Long id,@Valid @RequestBody DoctorUpdateDto doctor){
    return ResponseEntity.ok(doctorService.update(id, doctor));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Deleta um médico (soft delete)")
  @ApiResponse(responseCode = "204")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    doctorService.deactivate(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/me")
  @Operation(summary = "Retorna dados do médico autenticado")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> getMyData(@AuthenticationPrincipal User user) {
    return ResponseEntity.ok(doctorService.findByEmail(user.getUsername()));
  }

  @PutMapping("/me")
  @Operation(summary = "Atualiza dados do médico autenticado")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> updateMyData(
      @AuthenticationPrincipal User user,
      @Valid @RequestBody DoctorUpdateDto dto
  ) {
    DoctorDto doctor = doctorService.findByEmail(user.getUsername());
    return ResponseEntity.ok(doctorService.update(doctor.id(), dto));
  }
}
