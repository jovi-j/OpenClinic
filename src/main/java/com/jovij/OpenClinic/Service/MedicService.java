package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.InvalidDateFormatException;
import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Model.DTO.Medic.MedicDTO;
import com.jovij.OpenClinic.Model.DTO.Medic.MedicResponseDTO;
import com.jovij.OpenClinic.Model.DTO.Medic.MedicUpdateDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;
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
    public MedicResponseDTO create(MedicDTO medicDTO) {
        try {
            Person person = new Person();
            person.setName(medicDTO.person().name());
            person.setCpf(medicDTO.person().cpf());
            person.setBirthDate(LocalDate.parse(medicDTO.person().dateOfBirth(), DATE_FORMATTER));
            Person savedPerson = personRepository.save(person);

            Medic medic = new Medic();
            medic.setPerson(savedPerson);
            medic.setCrm(medicDTO.crm());
            medic.setType(medicDTO.type());

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
    public MedicResponseDTO update(UUID id, MedicUpdateDTO medicUpdateDTO) {
        try {
            Medic medic = medicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Medic not found"));
            Person person = medic.getPerson();

            if (medicUpdateDTO.person() != null) {
                if (medicUpdateDTO.person().name() != null) {
                    person.setName(medicUpdateDTO.person().name());
                }
                if (medicUpdateDTO.person().cpf() != null) {
                    person.setCpf(medicUpdateDTO.person().cpf());
                }
                if (medicUpdateDTO.person().dateOfBirth() != null) {
                    person.setBirthDate(LocalDate.parse(medicUpdateDTO.person().dateOfBirth(), DATE_FORMATTER));
                }
            }

            if (medicUpdateDTO.crm() != null) {
                medic.setCrm(medicUpdateDTO.crm());
            }
            if (medicUpdateDTO.type() != null) {
                medic.setType(medicUpdateDTO.type());
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
        PersonDTO personDTO = new PersonDTO(
                medic.getPerson().getName(),
                medic.getPerson().getCpf(),
                medic.getPerson().getBirthDate().format(DATE_FORMATTER)
        );
        return new MedicResponseDTO(medic.getId(), personDTO, medic.getCrm(), medic.getType());
    }
}
