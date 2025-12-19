package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.InvalidDateFormatException;
import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Model.DTO.Person.PatientResponseDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonUpdateDTO;
import com.jovij.OpenClinic.Model.Patient;
import com.jovij.OpenClinic.Model.Person;
import com.jovij.OpenClinic.Repository.PatientRepository;
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
public class PatientService {

    private final PatientRepository patientRepository;
    private final PersonRepository personRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    public PatientService(PatientRepository patientRepository, PersonRepository personRepository) {
        this.patientRepository = patientRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public PatientResponseDTO create(PersonDTO personDTO) {
        try {
            Person person = new Person();
            person.setName(personDTO.name());
            person.setCpf(personDTO.cpf());
            person.setBirthDate(LocalDate.parse(personDTO.dateOfBirth(), DATE_FORMATTER));
            Person savedPerson = personRepository.save(person);

            Patient patient = new Patient();
            patient.setPerson(savedPerson);
            patient.setMembershipId(generateMembershipId());

            Patient savedPatient = patientRepository.save(patient);
            return mapToDTO(savedPatient);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public List<PatientResponseDTO> findAll() {
        return patientRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private String generateMembershipId() {
        long patientCount = patientRepository.count();
        return String.format("%06d", patientCount + 1);
    }

    @Transactional
    public PatientResponseDTO update(UUID id, PersonUpdateDTO personUpdateDTO) {
        try {
            Patient patient = patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
            Person person = patient.getPerson();

            if (personUpdateDTO.name() != null) {
                person.setName(personUpdateDTO.name());
            }
            if (personUpdateDTO.cpf() != null) {
                person.setCpf(personUpdateDTO.cpf());
            }
            if (personUpdateDTO.dateOfBirth() != null) {
                person.setBirthDate(LocalDate.parse(personUpdateDTO.dateOfBirth(), DATE_FORMATTER));
            }

            personRepository.save(person);
            return mapToDTO(patient);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public void delete(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found");
        }
        patientRepository.deleteById(id);
    }

    private PatientResponseDTO mapToDTO(Patient patient) {
        PersonDTO personDTO = new PersonDTO(
                patient.getPerson().getName(),
                patient.getPerson().getCpf(),
                patient.getPerson().getBirthDate().format(DATE_FORMATTER)
        );
        return new PatientResponseDTO(patient.getId(), personDTO, patient.getMembershipId());
    }
}
