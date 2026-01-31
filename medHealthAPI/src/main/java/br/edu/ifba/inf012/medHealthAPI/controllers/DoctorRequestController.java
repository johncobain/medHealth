package br.edu.ifba.inf012.medHealthAPI.controllers;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest.DoctorRequestDto;
import br.edu.ifba.inf012.medHealthAPI.services.DoctorRequestService;
import br.edu.ifba.inf012.medHealthAPI.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/doctors-request")
public class DoctorRequestController {
    private final DoctorRequestService doctorRequestService;

    public DoctorRequestController(DoctorRequestService doctorRequestService, EmailService emailService){
        this.doctorRequestService = doctorRequestService;
    }

    @GetMapping
    @Operation(summary = "Retorna todas as solicitações de acesso de médicos")
    public ResponseEntity<Page<DoctorRequestDto>> findAll(
        @ParameterObject
        @PageableDefault(size = 10, sort = {"fullName"}, direction = Sort.Direction.ASC)
        Pageable pageable
    ){
        return ResponseEntity.ok(doctorRequestService.findAll(pageable));
    }

    @PostMapping("/accept/{id}")
    @Operation(summary = "Aprovar uma solicitação de acesso de médico")
    public ResponseEntity<DoctorDto> accept(@PathVariable Long id){
        DoctorDto dto = doctorRequestService.accept(id);
        return ResponseEntity.status(201).body(dto);
    }
    
    @PostMapping("/decline/{id}")
    @Operation(summary = "Reprovar uma solicitação de acesso de médico")
    public ResponseEntity<DoctorRequestDto> decline(@PathVariable Long id){
        DoctorRequestDto dto = doctorRequestService.decline(id);
        return ResponseEntity.status(200).body(dto);
    }

}
