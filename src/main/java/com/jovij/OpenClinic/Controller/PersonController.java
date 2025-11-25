package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonUpdateDTO;
import com.jovij.OpenClinic.Model.Person;
import com.jovij.OpenClinic.Service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> create(@RequestBody PersonDTO personDTO) {
        Person createdPerson = personService.create(personDTO);
        URI location = URI.create("/persons/" + createdPerson.getId());
        return ResponseEntity.created(location).body(createdPerson);
    }

    @GetMapping
    public ResponseEntity<List<Person>> listAll() {
        List<Person> persons = personService.findAll();
        return ResponseEntity.ok(persons);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> update(@PathVariable UUID id, @RequestBody PersonUpdateDTO personUpdateDTO) {
        Person updatedPerson = personService.update(id, personUpdateDTO);
        return ResponseEntity.ok(updatedPerson);
    }
}
