package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;
import com.jovij.OpenClinic.Model.Patient;
import com.jovij.OpenClinic.Service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public ResponseEntity<Patient> create(@RequestBody PersonDTO personDTO) {
        Patient createdPatient = patientService.create(personDTO);
        URI location = URI.create("/patients/" + createdPatient.getId());
        return ResponseEntity.created(location).body(createdPatient);
    }
}
