package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Medic.MedicDTO;
import com.jovij.OpenClinic.Model.Medic;
import com.jovij.OpenClinic.Service.MedicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/medics")
public class MedicController {

    private final MedicService medicService;

    @Autowired
    public MedicController(MedicService medicService) {
        this.medicService = medicService;
    }

    @PostMapping
    public ResponseEntity<Medic> create(@RequestBody MedicDTO medicDTO) {
        Medic createdMedic = medicService.create(medicDTO);
        URI location = URI.create("/medics/" + createdMedic.getId());
        return ResponseEntity.created(location).body(createdMedic);
    }

    @GetMapping
    public ResponseEntity<Iterable<Medic>> listAll() {
        Iterable<Medic> medics = medicService.listAll();
        return ResponseEntity.ok(medics);
    }
}
