package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Attendant.AttendantRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Attendant.AttendantResponseDTO;
import com.jovij.OpenClinic.Service.AttendantService;
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
@RequestMapping(value = "/attendants", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Attendants", description = "Endpoints for managing attendants")
public class AttendantController {

    private final AttendantService attendantService;

    @Autowired
    public AttendantController(AttendantService attendantService) {
        this.attendantService = attendantService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new attendant", description = "Creates a new attendant with the provided information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attendant created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<AttendantResponseDTO> create(@RequestBody AttendantRequestDTO attendantRequestDTO) {
        AttendantResponseDTO createdAttendant = attendantService.create(attendantRequestDTO);
        URI location = URI.create("/attendants/" + createdAttendant.id());
        return ResponseEntity.created(location).body(createdAttendant);
    }

    @GetMapping
    @Operation(summary = "List all attendants", description = "Retrieves a list of all registered attendants.")
    @ApiResponse(responseCode = "200", description = "List of attendants retrieved successfully")
    public ResponseEntity<List<AttendantResponseDTO>> listAll() {
        List<AttendantResponseDTO> attendants = attendantService.findAll();
        return ResponseEntity.ok(attendants);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update an attendant", description = "Updates an existing attendant's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attendant updated successfully"),
            @ApiResponse(responseCode = "404", description = "Attendant not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<AttendantResponseDTO> update(
            @Parameter(description = "ID of the attendant to update") @PathVariable UUID id,
            @RequestBody AttendantRequestDTO attendantRequestDTO) {
        AttendantResponseDTO updatedAttendant = attendantService.update(id, attendantRequestDTO);
        return ResponseEntity.ok(updatedAttendant);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an attendant", description = "Deletes an attendant by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attendant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Attendant not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the attendant to delete") @PathVariable UUID id) {
        attendantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
