package br.edu.ifba.inf012.medHealthAPI.services;

import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorUpdateDto;
import br.edu.ifba.inf012.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf012.medHealthAPI.exceptions.UniqueAttributeAlreadyRegisteredException;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Address;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Person;
import br.edu.ifba.inf012.medHealthAPI.models.enums.DoctorStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.PersonStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.Specialty;
import br.edu.ifba.inf012.medHealthAPI.repositories.AddressRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.DoctorRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
  private final DoctorRepository doctorRepository;
  private final PersonRepository personRepository;
  private final AddressRepository addressRepository;
  private final UserService userService;

  public DoctorService(
    DoctorRepository doctorRepository,
    PersonRepository personRepository,
    AddressRepository addressRepository,
    UserService userService
    ){
    this.doctorRepository = doctorRepository;
    this.personRepository = personRepository;
    this.addressRepository = addressRepository;
    this.userService = userService;
  }

  public Page<DoctorDto> findAll(Pageable pageable) {
    return this.doctorRepository.findAll(pageable)
        .map(DoctorDto::fromEntity);
  }

  public DoctorDto findById(Long id) {
    Doctor doctor = doctorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), id));
    return DoctorDto.fromEntity(doctor);
  }

  public DoctorDto findByEmail(String email) {
    Doctor doctor = doctorRepository.findByPersonEmail(email)
        .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), "email", email));
    return DoctorDto.fromEntity(doctor);
  }

  public DoctorDto findByCpf(String cpf) {
    Doctor doctor = doctorRepository.findByPersonCpf(cpf)
        .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), "cpf", cpf));
    return DoctorDto.fromEntity(doctor);
  }

  public DoctorDto findByCrm(String crm) {
    Doctor doctor = doctorRepository.findByCrm(crm)
        .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), "crm", crm));
    return DoctorDto.fromEntity(doctor);
  }

  public Page<DoctorDto> findBySpecialty(Specialty specialty, Pageable pageable) {
    return doctorRepository.findBySpecialty(specialty, pageable)
        .map(DoctorDto::fromEntity);
  }

  public Page<DoctorDto> findByStatus(DoctorStatus status, Pageable pageable) {
    return doctorRepository.findByStatus(status, pageable)
        .map(DoctorDto::fromEntity);
  }

  public Page<DoctorDto> findBySpecialtyAndStatus(Specialty specialty, DoctorStatus status, Pageable pageable) {
    return doctorRepository.findBySpecialtyAndStatus(specialty, status, pageable)
        .map(DoctorDto::fromEntity);
  }

  @Transactional
  public DoctorDto save(DoctorFormDto dto){
    if (personRepository.existsByEmail(dto.email())) {
      throw new UniqueAttributeAlreadyRegisteredException(Doctor.class.getSimpleName(), "email");
    }
    if (personRepository.existsByCpf(dto.cpf())) {
      throw new UniqueAttributeAlreadyRegisteredException(Doctor.class.getSimpleName(), "cpf");
    }
    if (doctorRepository.existsByCrm(dto.crm())) {
      throw new UniqueAttributeAlreadyRegisteredException(Doctor.class.getSimpleName(), "crm");
    }

    Address address = new Address(dto.address());
    address = addressRepository.save(address);

    Person person = new Person(
        dto.fullName(),
        dto.email(),
        dto.phone(),
        dto.cpf(),
        address
    );
    person = personRepository.save(person);

    Doctor doctor = new Doctor(person, dto.crm(), dto.specialty());
    doctor = doctorRepository.save(doctor);

    userService.createUserForPerson(person, true);

    return DoctorDto.fromEntity(doctor);
  }

  @Transactional
  public DoctorDto update(Long id, DoctorUpdateDto doctor){
    Doctor storedDoctor = this.doctorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), id));

    if(doctor.fullName() != null){
      storedDoctor.getPerson().setFullName(doctor.fullName());
    }

    if(doctor.phone() != null){
      storedDoctor.getPerson().setPhone(doctor.phone());
    }

    if(doctor.address() != null){
      storedDoctor.getPerson().setAddress(addressRepository.save(new Address(doctor.address())));
    }
    this.personRepository.save(storedDoctor.getPerson());

    return DoctorDto.fromEntity(storedDoctor);
  }

  @Transactional
  public void deactivate(Long id) {
    Doctor doctor = doctorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), id));

    doctor.setStatus(DoctorStatus.INACTIVE);
    doctor.getPerson().setStatus(PersonStatus.INACTIVE);

    personRepository.save(doctor.getPerson());
    doctorRepository.save(doctor);
  }

  @Transactional
  public void activate(Long id) {
    Doctor doctor = doctorRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Doctor.class.getSimpleName(), id));

    doctor.setStatus(DoctorStatus.ACTIVE);
    doctor.getPerson().setStatus(PersonStatus.ACTIVE);

    personRepository.save(doctor.getPerson());
    doctorRepository.save(doctor);
  }

  @Transactional
  public void delete(Long id) {
    if (!doctorRepository.existsById(id)) {
      throw new EntityNotFoundException(Doctor.class.getSimpleName(), id);
    }
    doctorRepository.deleteById(id);
  }

  public long count() {
    return doctorRepository.count();
  }
}
