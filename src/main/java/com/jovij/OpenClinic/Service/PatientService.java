package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;
import com.jovij.OpenClinic.Model.Patient;
import com.jovij.OpenClinic.Model.Person;
import com.jovij.OpenClinic.Repository.PatientRepository;
import com.jovij.OpenClinic.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PatientService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final PatientRepository patientRepository;
    private final PersonRepository personRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, PersonRepository personRepository) {
        this.patientRepository = patientRepository;
        this.personRepository = personRepository;
    }

    @Transactional
    public Patient create(PersonDTO personDTO) {
        Person person = new Person();
        person.setName(personDTO.name());
        person.setCpf(personDTO.cpf());
        person.setBirthDate(LocalDate.parse(personDTO.dateOfBirth(), DATE_FORMATTER));
        Person savedPerson = personRepository.save(person);

        Patient patient = new Patient();
        patient.setPerson(savedPerson);
        patient.setMembershipId(generateMembershipId());

        return patientRepository.save(patient);
    }

    private String generateMembershipId() {
        long patientCount = patientRepository.count();
        return String.format("%06d", patientCount + 1);
    }
}
