package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Person.PatientResponseDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;
import com.jovij.OpenClinic.Model.DTO.Person.PersonUpdateDTO;
import com.jovij.OpenClinic.Service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "Endpoints for managing patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @Operation(summary = "Create a new patient")
    public ResponseEntity<PatientResponseDTO> create(@RequestBody PersonDTO personDTO) {
        PatientResponseDTO createdPatient = patientService.create(personDTO);
        URI location = URI.create("/patients/" + createdPatient.id());
        return ResponseEntity.created(location).body(createdPatient);
    }

    @GetMapping
    @Operation(summary = "List all patients")
    public ResponseEntity<List<PatientResponseDTO>> listAll() {
        List<PatientResponseDTO> patients = patientService.findAll();
        return ResponseEntity.ok(patients);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a patient")
    public ResponseEntity<PatientResponseDTO> update(@PathVariable UUID id, @RequestBody PersonUpdateDTO personUpdateDTO) {
        PatientResponseDTO updatedPatient = patientService.update(id, personUpdateDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a patient")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
