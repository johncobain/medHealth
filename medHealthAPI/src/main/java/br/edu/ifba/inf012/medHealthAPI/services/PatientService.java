package br.edu.ifba.inf012.medHealthAPI.services;

import br.edu.ifba.inf012.medHealthAPI.dtos.patient.PatientDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.patient.PatientFormDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.patient.PatientUpdateDto;
import br.edu.ifba.inf012.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf012.medHealthAPI.exceptions.UniqueAttributeAlreadyRegisteredException;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Address;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Patient;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Person;
import br.edu.ifba.inf012.medHealthAPI.models.enums.PatientStatus;
import br.edu.ifba.inf012.medHealthAPI.models.enums.PersonStatus;
import br.edu.ifba.inf012.medHealthAPI.repositories.AddressRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.PatientRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PatientService {
  private final PatientRepository patientRepository;
  private final PersonRepository personRepository;
  private final AddressRepository addressRepository;
  private final UserService userService;

  public PatientService(
      PatientRepository patientRepository,
      PersonRepository personRepository,
      AddressRepository addressRepository,
      UserService userService
      ){
      this.patientRepository = patientRepository;
      this.personRepository = personRepository;
      this.addressRepository = addressRepository;
      this.userService = userService;
  }

  public Page<PatientDto> findAll(Pageable pageable) {
      return this.patientRepository.findAll(pageable)
          .map(PatientDto::fromEntity);
  }

  public PatientDto findById(Long id){
      Patient patient = this.patientRepository.findById(id)
          .orElseThrow(() -> new EntityNotFoundException(Patient.class.getSimpleName(), id));
      return PatientDto.fromEntity(patient);
  }

  public PatientDto findByEmail(String email) {
    Patient patient = patientRepository.findByPersonEmail(email)
        .orElseThrow(() -> new EntityNotFoundException(Patient.class.getSimpleName(), "email", email));
    return PatientDto.fromEntity(patient);
  }

  public PatientDto findByCpf(String cpf) {
    Patient patient = patientRepository.findByPersonCpf(cpf)
        .orElseThrow(() -> new EntityNotFoundException(Patient.class.getSimpleName(), "cpf", cpf));
    return PatientDto.fromEntity(patient);
  }

  public Page<PatientDto> findByStatus(PatientStatus status, Pageable pageable) {
    return patientRepository.findByStatus(status, pageable)
        .map(PatientDto::fromEntity);
  }

  @Transactional
  public PatientDto save(PatientFormDto dto){
    if (personRepository.existsByEmail(dto.email())) {
      throw new UniqueAttributeAlreadyRegisteredException(Patient.class.getSimpleName(), "email");
    }
    if (personRepository.existsByCpf(dto.cpf())) {
      throw new UniqueAttributeAlreadyRegisteredException(Patient.class.getSimpleName(), "cpf");
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

    Patient patient = new Patient(person);
    patient = patientRepository.save(patient);

    userService.createUserForPerson(person, false);

    return PatientDto.fromEntity(patient);
  }

  @Transactional
  public PatientDto update(Long id, PatientUpdateDto patient){
    Patient storedPatient = patientRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Patient.class.getSimpleName(), id));

    if(patient.fullName() != null){
      storedPatient.getPerson().setFullName(patient.fullName());
    }

    if(patient.phone() != null){
      storedPatient.getPerson().setPhone(patient.phone());
    }

    if(patient.address() != null){
      storedPatient.getPerson().setAddress(addressRepository.save(new Address(patient.address())));
    }
    this.personRepository.save(storedPatient.getPerson());

    return PatientDto.fromEntity(storedPatient);
  }

  @Transactional
  public void deactivate(Long id) {
    Patient patient = patientRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Patient.class.getSimpleName(), id));

    patient.setStatus(PatientStatus.INACTIVE);
    patient.getPerson().setStatus(PersonStatus.INACTIVE);

    personRepository.save(patient.getPerson());
    patientRepository.save(patient);
  }

  @Transactional
  public void activate(Long id) {
    Patient patient = patientRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(Patient.class.getSimpleName(), id));

    patient.setStatus(PatientStatus.ACTIVE);
    patient.getPerson().setStatus(PersonStatus.ACTIVE);

    personRepository.save(patient.getPerson());
    patientRepository.save(patient);
  }

  @Transactional
  public void delete(Long id) {
    if (!patientRepository.existsById(id)) {
      throw new EntityNotFoundException(Patient.class.getSimpleName(), id);
    }
    patientRepository.deleteById(id);
  }
}
