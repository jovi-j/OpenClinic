package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonUpdateDTO;
import com.jovij.OpenClinic.Model.Person;
import com.jovij.OpenClinic.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class PersonService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Transactional
    public Person create(PersonDTO personDTO) {
        Person person = new Person();
        person.setName(personDTO.name());
        person.setCpf(personDTO.cpf());
        person.setBirthDate(LocalDate.parse(personDTO.dateOfBirth(), DATE_FORMATTER));
        return personRepository.save(person);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Transactional
    public Person update(UUID id, PersonUpdateDTO personUpdateDTO) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));

        if (personUpdateDTO.name() != null) {
            person.setName(personUpdateDTO.name());
        }
        if (personUpdateDTO.cpf() != null) {
            person.setCpf(personUpdateDTO.cpf());
        }
        if (personUpdateDTO.dateOfBirth() != null) {
            person.setBirthDate(LocalDate.parse(personUpdateDTO.dateOfBirth(), DATE_FORMATTER));
        }

        return personRepository.save(person);
    }
}
