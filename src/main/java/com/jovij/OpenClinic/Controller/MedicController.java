package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Medic.MedicDTO;
import com.jovij.OpenClinic.Model.DTO.Medic.MedicResponseDTO;
import com.jovij.OpenClinic.Model.DTO.Medic.MedicUpdateDTO;
import com.jovij.OpenClinic.Service.MedicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medics")
@Tag(name = "Medics", description = "Endpoints for managing medics")
public class MedicController {

    private final MedicService medicService;

    @Autowired
    public MedicController(MedicService medicService) {
        this.medicService = medicService;
    }

    @PostMapping
    @Operation(summary = "Create a new medic")
    public ResponseEntity<MedicResponseDTO> create(@RequestBody MedicDTO medicDTO) {
        MedicResponseDTO createdMedic = medicService.create(medicDTO);
        URI location = URI.create("/medics/" + createdMedic.id());
        return ResponseEntity.created(location).body(createdMedic);
    }

    @GetMapping
    @Operation(summary = "List all medics")
    public ResponseEntity<List<MedicResponseDTO>> listAll() {
        List<MedicResponseDTO> medics = medicService.listAll();
        return ResponseEntity.ok(medics);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a medic")
    public ResponseEntity<MedicResponseDTO> update(@PathVariable UUID id, @RequestBody MedicUpdateDTO medicUpdateDTO) {
        MedicResponseDTO updatedMedic = medicService.update(id, medicUpdateDTO);
        return ResponseEntity.ok(updatedMedic);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a medic")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        medicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
