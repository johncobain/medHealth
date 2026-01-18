package br.edu.ifba.inf015.medHealthAPI.controllers;

import br.edu.ifba.inf015.medHealthAPI.dtos.doctor.DoctorDto;
import br.edu.ifba.inf015.medHealthAPI.dtos.doctor.DoctorFormDto;
import br.edu.ifba.inf015.medHealthAPI.dtos.doctor.DoctorUpdateDto;
import br.edu.ifba.inf015.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf015.medHealthAPI.services.DoctorService;
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
@RequestMapping("/doctors")
public class DoctorController {
  private final DoctorService doctorService;

  public DoctorController(DoctorService doctorService){
    this.doctorService = doctorService;
  }

  @GetMapping
  @Operation(summary = "Get All active Doctors")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<Page<DoctorDto>> getAll(
    @ParameterObject
    @PageableDefault(size = 10, sort = {"name"}, direction = Sort.Direction.ASC)
    Pageable pageable
  ){
    return ResponseEntity.ok(doctorService.getAll(pageable));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get an active Doctor")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> getOne(@PathVariable Long id){
    return ResponseEntity.ok(doctorService.getOne(id));
  }

  @PostMapping
  @Operation(summary = "Create a new Doctor")
  @ApiResponse(responseCode = "201")
  public ResponseEntity<DoctorDto> create(@Valid @RequestBody DoctorFormDto doctorFormDto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.save(doctorFormDto));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update an active Doctor")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> update(@Valid @RequestBody DoctorUpdateDto doctorUpdateDto, @PathVariable Long id){
    return ResponseEntity.ok(doctorService.update(doctorUpdateDto, id));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an active Doctor")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<DoctorDto> delete(@PathVariable Long id){
    return ResponseEntity.ok(doctorService.delete(id));
  }
}
