package br.edu.ifba.inf015.medHealthAPI.services;

import br.edu.ifba.inf015.medHealthAPI.dtos.patient.PatientDto;
import br.edu.ifba.inf015.medHealthAPI.dtos.patient.PatientFormDto;
import br.edu.ifba.inf015.medHealthAPI.dtos.patient.PatientUpdateDto;
import br.edu.ifba.inf015.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf015.medHealthAPI.exceptions.UniqueAttributeAlreadyRegisteredException;
import br.edu.ifba.inf015.medHealthAPI.models.entities.Address;
import br.edu.ifba.inf015.medHealthAPI.models.entities.Patient;
import br.edu.ifba.inf015.medHealthAPI.repositories.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public Page<PatientDto> getAll(Pageable pageable) {
        return this.patientRepository.findByStatus(pageable, "ACTIVE").map(PatientDto::new);
    }

    public PatientDto getOne(Long id){
        Patient patient = this.patientRepository.findByIdAndStatus(id, "ACTIVE");
        if (patient == null) throw new EntityNotFoundException(Patient.class.getSimpleName(), id);
        return PatientDto.fromEntity(patient);
    }

    @Transactional
    public PatientDto save(PatientFormDto patient){
        if (this.patientRepository.existsByCpfAndStatus(patient.cpf(), "ACTIVE")){
            throw new UniqueAttributeAlreadyRegisteredException(Patient.class.getSimpleName(), "CPF");
        }

        if (this.patientRepository.existsByCpfAndStatus(patient.cpf(), "INACTIVE")){
          Patient storedPatient = this.patientRepository.findByCpfAndStatus(patient.cpf(), "INACTIVE");
          storedPatient.setStatus("ACTIVE");
          storedPatient.setName(patient.name());
          storedPatient.setPhone(patient.phone());
          storedPatient.setAddress(new Address(patient.address()));
          return PatientDto.fromEntity(this.patientRepository.save(storedPatient));
        }

        return PatientDto.fromEntity(this.patientRepository.save(new Patient(patient)));
    }

    @Transactional
    public PatientDto update(PatientUpdateDto patient, Long id){
      Patient storedPatient = this.patientRepository.findByIdAndStatus(id, "ACTIVE");
      if (storedPatient == null) throw new EntityNotFoundException(Patient.class.getSimpleName(), id);

      if(patient.name() != null){
        storedPatient.setName(patient.name());
      }

      if(patient.phone() != null){
        storedPatient.setPhone(patient.phone());
      }

      if(patient.address() != null){
        storedPatient.setAddress(new Address(patient.address()));
      }

      return PatientDto.fromEntity(this.patientRepository.save(storedPatient));
    }

    @Transactional
    public PatientDto delete(Long id){
      Patient storedPatient = this.patientRepository.findByIdAndStatus(id, "ACTIVE");
      if (storedPatient == null) throw new EntityNotFoundException(Patient.class.getSimpleName(), id);
      storedPatient.setStatus("INACTIVE");
      return PatientDto.fromEntity(this.patientRepository.save(storedPatient));
    }
}
