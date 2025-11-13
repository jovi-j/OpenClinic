package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Model.DTO.Medic.MedicDTO;
import com.jovij.OpenClinic.Model.Medic;
import com.jovij.OpenClinic.Model.Person;
import com.jovij.OpenClinic.Repository.MedicRepository;
import com.jovij.OpenClinic.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    public Medic create(MedicDTO medicDTO) {
        Person person = new Person();
        person.setName(medicDTO.person().name());
        person.setCpf(medicDTO.person().cpf());
        person.setBirthDate(LocalDate.parse(medicDTO.person().dateOfBirth(), DATE_FORMATTER));
        Person savedPerson = personRepository.save(person);

        Medic medic = new Medic();
        medic.setPerson(savedPerson);
        medic.setCrm(medicDTO.crm());
        medic.setType(medicDTO.type());

        return medicRepository.save(medic);
    }

    public Iterable<Medic> listAll() {
        return medicRepository.findAll();
    }
}
