package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Person.PersonRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonResponseDTO;
import com.jovij.OpenClinic.Service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/persons", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Persons", description = "Endpoints for managing persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new person", description = "Creates a new person with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<PersonResponseDTO> create(@RequestBody PersonRequestDTO personRequestDTO) {
        PersonResponseDTO createdPerson = personService.create(personRequestDTO);
        URI location = URI.create("/persons/" + createdPerson.id());
        return ResponseEntity.created(location).body(createdPerson);
    }

    @GetMapping
    @Operation(summary = "List all persons", description = "Retrieves a list of all registered persons.")
    @ApiResponse(responseCode = "200", description = "List of persons retrieved successfully")
    public ResponseEntity<List<PersonResponseDTO>> listAll() {
        List<PersonResponseDTO> persons = personService.findAll();
        return ResponseEntity.ok(persons);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a person", description = "Updates an existing person's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person updated successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<PersonResponseDTO> update(
            @Parameter(description = "ID of the person to update") @PathVariable UUID id,
            @RequestBody PersonRequestDTO personRequestDTO) {
        PersonResponseDTO updatedPerson = personService.update(id, personRequestDTO);
        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a person", description = "Deletes a person by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the person to delete") @PathVariable UUID id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
