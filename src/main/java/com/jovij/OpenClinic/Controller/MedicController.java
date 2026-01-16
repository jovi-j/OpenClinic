package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Medic.MedicRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Medic.MedicResponseDTO;
import com.jovij.OpenClinic.Service.MedicService;
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
@RequestMapping(value = "/medics", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Medics", description = "Endpoints for managing medics")
public class MedicController {

    private final MedicService medicService;

    @Autowired
    public MedicController(MedicService medicService) {
        this.medicService = medicService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new medic", description = "Creates a new medic with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medic created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<MedicResponseDTO> create(@RequestBody MedicRequestDTO medicRequestDTO) {
        MedicResponseDTO createdMedic = medicService.create(medicRequestDTO);
        URI location = URI.create("/medics/" + createdMedic.id());
        return ResponseEntity.created(location).body(createdMedic);
    }

    @GetMapping
    @Operation(summary = "List all medics", description = "Retrieves a list of all registered medics.")
    @ApiResponse(responseCode = "200", description = "List of medics retrieved successfully")
    public ResponseEntity<List<MedicResponseDTO>> listAll() {
        List<MedicResponseDTO> medics = medicService.listAll();
        return ResponseEntity.ok(medics);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a medic", description = "Updates an existing medic's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medic updated successfully"),
            @ApiResponse(responseCode = "404", description = "Medic not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<MedicResponseDTO> update(
            @Parameter(description = "ID of the medic to update") @PathVariable UUID id,
            @RequestBody MedicRequestDTO medicRequestDTO) {
        MedicResponseDTO updatedMedic = medicService.update(id, medicRequestDTO);
        return ResponseEntity.ok(updatedMedic);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a medic", description = "Deletes a medic by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medic deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Medic not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the medic to delete") @PathVariable UUID id) {
        medicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
