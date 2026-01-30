package br.edu.ifba.inf012.medHealthAPI.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.ifba.inf012.medHealthAPI.dtos.address.AddressDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctor.DoctorDto;
import br.edu.ifba.inf012.medHealthAPI.dtos.doctorRequest.DoctorRequestDto;
import br.edu.ifba.inf012.medHealthAPI.exceptions.EntityNotFoundException;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Address;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Doctor;
import br.edu.ifba.inf012.medHealthAPI.models.entities.Person;
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

    public DoctorRequestService(DoctorRequestRepository doctorRequestRepository, AddressRepository addressRepository, PersonRepository personRepository, DoctorRepository doctorRepository, UserService userService){
        this.doctorRequestRepository = doctorRequestRepository;
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.doctorRepository = doctorRepository;
        this.userService = userService;
    }

    public Page<DoctorRequestDto> findAll(Pageable pageable){
        return this.doctorRequestRepository.findAll(pageable)
            .map(DoctorRequestDto::fromEntity);
    }

    @Transactional
    public DoctorDto accept(Long id) {
        DoctorRequestDto dto = findById(id);

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

        userService.createUserForPerson(person, true);

        return DoctorDto.fromEntity(doctor);
    }

    private DoctorRequestDto findById(Long id) {
        var doctorRequest = doctorRequestRepository.findById(id);

        if (!doctorRequest.isPresent())
            throw new EntityNotFoundException("Solicitação de acesso de médico não encontrada.");

        return DoctorRequestDto.fromEntity(doctorRequest.get());
    }
}
