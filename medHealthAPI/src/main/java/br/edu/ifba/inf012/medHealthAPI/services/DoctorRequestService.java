package br.edu.ifba.inf012.medHealthAPI.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest.DoctorRequestDto;
import br.edu.ifba.inf012.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Address;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf012.medHealthAPI.models.entities.DoctorRequest;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Person;
import br.edu.ifba.inf012.medHealthAPI.models.enums.DoctorRequestStatus;
import br.edu.ifba.inf012.medHealthAPI.repositories.AddressRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.DoctorRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.DoctorRequestRepository;
import br.edu.ifba.inf012.medHealthAPI.repositories.PersonRepository;
import jakarta.transaction.Transactional;

@Service
public class DoctorRequestService {
    private final DoctorRequestRepository doctorRequestRepository;
    private final AddressRepository addressRepository;
    private final PersonRepository personRepository;
    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final EmailService emailService;

    public DoctorRequestService(DoctorRequestRepository doctorRequestRepository, AddressRepository addressRepository, PersonRepository personRepository, DoctorRepository doctorRepository, UserService userService, EmailService emailService){
        this.doctorRequestRepository = doctorRequestRepository;
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.emailService = emailService;
    }

    public Page<DoctorRequestDto> findAll(Pageable pageable){
        return this.doctorRequestRepository.findAll(pageable)
            .map(DoctorRequestDto::fromEntity);
    }

    @Transactional
    public DoctorDto accept(Long id) {
        DoctorRequest doctorRequest = findById(id);

        if(doctorRequest.getStatus() != DoctorRequestStatus.PENDING)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta solicitação já foi processada (Status = "+doctorRequest.getStatus()+")");

        DoctorRequestDto dto = new DoctorRequestDto(doctorRequest);

        Address address = new Address(new AddressDto(dto));
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

        doctorRequest.setStatus(DoctorRequestStatus.APROVED);
        doctorRequestRepository.save(doctorRequest);

        userService.createUserForPerson(person, true);

        return DoctorDto.fromEntity(doctor);
    }

    public DoctorRequestDto decline(Long id) {
        DoctorRequest doctorRequest = findById(id);
        
        if(doctorRequest.getStatus() != DoctorRequestStatus.PENDING)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta solicitação já foi processada (Status = "+doctorRequest.getStatus()+")");

        doctorRequest.setStatus(DoctorRequestStatus.DECLINED);

        doctorRequestRepository.save(doctorRequest);

        DoctorRequestDto dto = new DoctorRequestDto(doctorRequest);
        
        emailService.sendDoctorRequestDeclined(dto);

        return dto;
    }

    private DoctorRequest findById(Long id) {
        var doctorRequest = doctorRequestRepository.findById(id);

        if (!doctorRequest.isPresent())
            throw new EntityNotFoundException("Solicitação de acesso de médico não encontrada.");

        return doctorRequest.get();
    }

}
