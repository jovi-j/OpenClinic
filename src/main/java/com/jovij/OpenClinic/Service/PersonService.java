package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.InvalidDateFormatException;
import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Model.DTO.Person.PersonRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonResponseDTO;
import com.jovij.OpenClinic.Model.Person;
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
public class PersonService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public PersonResponseDTO create(PersonRequestDTO personRequestDTO) {
        try {
            Person person = new Person();
            person.setName(personRequestDTO.name());
            person.setCpf(personRequestDTO.cpf());
            person.setBirthDate(LocalDate.parse(personRequestDTO.dateOfBirth(), DATE_FORMATTER));
            Person savedPerson = personRepository.save(person);
            return mapToDTO(savedPerson);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public List<PersonResponseDTO> findAll() {
        return personRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PersonResponseDTO update(UUID id, PersonRequestDTO personRequestDTO) {
        try {
            Person person = personRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + id));

            if (personRequestDTO.name() != null) {
                person.setName(personRequestDTO.name());
            }
            if (personRequestDTO.cpf() != null) {
                person.setCpf(personRequestDTO.cpf());
            }
            if (personRequestDTO.dateOfBirth() != null) {
                person.setBirthDate(LocalDate.parse(personRequestDTO.dateOfBirth(), DATE_FORMATTER));
            }

            Person savedPerson = personRepository.save(person);
            return mapToDTO(savedPerson);
        } catch (DateTimeParseException e) {
            throw new InvalidDateFormatException("Invalid date format. Please use dd/MM/yyyy.");
        }
    }

    public void delete(UUID id) {
        if (!personRepository.existsById(id)) {
            throw new ResourceNotFoundException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);
    }

    private PersonResponseDTO mapToDTO(Person person) {
        return new PersonResponseDTO(
                person.getId(),
                person.getName(),
                person.getCpf(),
                person.getBirthDate().format(DATE_FORMATTER)
        );
    }

}
