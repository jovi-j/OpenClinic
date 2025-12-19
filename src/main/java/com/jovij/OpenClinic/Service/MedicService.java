package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.InvalidDateFormatException;
import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Model.DTO.Medic.MedicRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Medic.MedicResponseDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonResponseDTO;
import com.jovij.OpenClinic.Model.Medic;
import com.jovij.OpenClinic.Model.Person;
import com.jovij.OpenClinic.Repository.MedicRepository;
import com.jovij.OpenClinic.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MedicService {

    private final MedicRepository medicRepository;
    private final PersonRepository personRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    @Autowired
    public MedicService(MedicRepository medicRepository, PersonRepository personRepository) {
        this.medicRepository = medicRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public MedicResponseDTO create(MedicRequestDTO medicRequestDTO) {
        try {
            Person person = new Person();
            person.setName(medicRequestDTO.person().name());
            person.setCpf(medicRequestDTO.person().cpf());
            person.setBirthDate(LocalDate.parse(medicRequestDTO.person().dateOfBirth(), DATE_FORMATTER));
            Person savedPerson = personRepository.save(person);

            Medic medic = new Medic();
            medic.setPerson(savedPerson);
            medic.setCrm(medicRequestDTO.crm());
            medic.setType(medicRequestDTO.type());

            Medic savedMedic = medicRepository.save(medic);
            return mapToDTO(savedMedic);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public List<MedicResponseDTO> listAll() {
        return medicRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MedicResponseDTO update(UUID id, MedicRequestDTO medicRequestDTO) {
        try {
            Medic medic = medicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medic not found"));
            Person person = medic.getPerson();

            if (medicRequestDTO.person() != null) {
                if (medicRequestDTO.person().name() != null) {
                    person.setName(medicRequestDTO.person().name());
                }
                if (medicRequestDTO.person().cpf() != null) {
                    person.setCpf(medicRequestDTO.person().cpf());
                }
                if (medicRequestDTO.person().dateOfBirth() != null) {
                    person.setBirthDate(LocalDate.parse(medicRequestDTO.person().dateOfBirth(), DATE_FORMATTER));
                }
            }

            if (medicRequestDTO.crm() != null) {
                medic.setCrm(medicRequestDTO.crm());
            }
            if (medicRequestDTO.type() != null) {
                medic.setType(medicRequestDTO.type());
            }

            personRepository.save(person);
            Medic savedMedic = medicRepository.save(medic);
            return mapToDTO(savedMedic);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public void delete(UUID id) {
        if (!medicRepository.existsById(id)) {
            throw new ResourceNotFoundException("Medic not found");
        }
        medicRepository.deleteById(id);
    }

    private MedicResponseDTO mapToDTO(Medic medic) {
        PersonResponseDTO personResponseDTO = new PersonResponseDTO(
                medic.getPerson().getId(),
                medic.getPerson().getName(),
                medic.getPerson().getCpf(),
                medic.getPerson().getBirthDate().format(DATE_FORMATTER)
        );
        return new MedicResponseDTO(medic.getId(), personResponseDTO, medic.getCrm(), medic.getType());
    }
}
