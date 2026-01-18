package br.edu.ifba.inf015.medHealthAPI.services;

import br.edu.ifba.inf015.medHealthAPI.dtos.doctor.DoctorDto;
import br.edu.ifba.inf015.medHealthAPI.dtos.doctor.DoctorFormDto;
import br.edu.ifba.inf015.medHealthAPI.dtos.doctor.DoctorUpdateDto;
import br.edu.ifba.inf015.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf015.medHealthAPI.exceptions.UniqueAttributeAlreadyRegisteredException;
import br.edu.ifba.inf015.medHealthAPI.models.entities.Address;
import br.edu.ifba.inf015.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf015.medHealthAPI.repositories.DoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
  private final DoctorRepository doctorRepository;

  public DoctorService(DoctorRepository doctorRepository){
    this.doctorRepository = doctorRepository;
  }

  public Page<DoctorDto> getAll(Pageable pageable) {
    return this.doctorRepository.findByStatus(pageable, "ACTIVE").map(DoctorDto::new);
  }

  public DoctorDto getOne(Long id){
    Doctor doctor = this.doctorRepository.findByIdAndStatus(id, "ACTIVE");
    if (doctor == null) throw new EntityNotFoundException(Doctor.class.getSimpleName(), id);
    return DoctorDto.fromEntity(doctor);
  }

  @Transactional
  public DoctorDto save(DoctorFormDto doctor){
    if (this.doctorRepository.existsByCrmAndStatus(doctor.crm(), "ACTIVE")){
      throw new UniqueAttributeAlreadyRegisteredException(Doctor.class.getSimpleName(), "CRM");
    }

    if (this.doctorRepository.existsByCrmAndStatus(doctor.crm(), "INACTIVE")){
      Doctor storedDoctor = this.doctorRepository.findByCrmAndStatus(doctor.crm(), "INACTIVE");
      storedDoctor.setStatus("ACTIVE");
      storedDoctor.setName(doctor.name());
      storedDoctor.setPhone(doctor.phone());
      storedDoctor.setAddress(new Address(doctor.address()));
      return DoctorDto.fromEntity(this.doctorRepository.save(storedDoctor));
    }

    return DoctorDto.fromEntity(this.doctorRepository.save(new Doctor(doctor)));
  }

  @Transactional
  public DoctorDto update(DoctorUpdateDto doctor, Long id){
    Doctor storedDoctor = this.doctorRepository.findByIdAndStatus(id, "ACTIVE");
    if (storedDoctor == null) throw new EntityNotFoundException(Doctor.class.getSimpleName(), id);

    if(doctor.name() != null){
      storedDoctor.setName(doctor.name());
    }

    if(doctor.phone() != null){
      storedDoctor.setPhone(doctor.phone());
    }

    if(doctor.address() != null){
      storedDoctor.setAddress(new Address(doctor.address()));
    }

    return DoctorDto.fromEntity(this.doctorRepository.save(storedDoctor));
  }

  @Transactional
  public DoctorDto delete(Long id){
    Doctor storedDoctor = this.doctorRepository.findByIdAndStatus(id, "ACTIVE");
    if (storedDoctor == null) throw new EntityNotFoundException(Doctor.class.getSimpleName(), id);
    storedDoctor.setStatus("INACTIVE");
    return DoctorDto.fromEntity(this.doctorRepository.save(storedDoctor));
  }
}
