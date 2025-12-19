package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonResponseDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonUpdateDTO;
import com.jovij.OpenClinic.Service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/persons")
@Tag(name = "Persons", description = "Endpoints for managing persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @Operation(summary = "Create a new person")
    public ResponseEntity<PersonResponseDTO> create(@RequestBody PersonDTO personDTO) {
        PersonResponseDTO createdPerson = personService.create(personDTO);
        URI location = URI.create("/persons/" + createdPerson.id());
        return ResponseEntity.created(location).body(createdPerson);
    }

    @GetMapping
    @Operation(summary = "List all persons")
    public ResponseEntity<List<PersonResponseDTO>> listAll() {
        List<PersonResponseDTO> persons = personService.findAll();
        return ResponseEntity.ok(persons);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a person")
    public ResponseEntity<PersonResponseDTO> update(@PathVariable UUID id, @RequestBody PersonUpdateDTO personUpdateDTO) {
        PersonResponseDTO updatedPerson = personService.update(id, personUpdateDTO);
        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a person")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
