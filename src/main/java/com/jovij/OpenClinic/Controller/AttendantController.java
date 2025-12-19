package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Attendant.AttendantRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Attendant.AttendantResponseDTO;
import com.jovij.OpenClinic.Service.AttendantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/attendants")
@Tag(name = "Attendants", description = "Endpoints for managing attendants")
public class AttendantController {

    private final AttendantService attendantService;

    @Autowired
    public AttendantController(AttendantService attendantService) {
        this.attendantService = attendantService;
    }

    @PostMapping
    @Operation(summary = "Create a new attendant")
    public ResponseEntity<AttendantResponseDTO> create(@RequestBody AttendantRequestDTO attendantRequestDTO) {
        AttendantResponseDTO createdAttendant = attendantService.create(attendantRequestDTO);
        URI location = URI.create("/attendants/" + createdAttendant.id());
        return ResponseEntity.created(location).body(createdAttendant);
    }

    @GetMapping
    @Operation(summary = "List all attendants")
    public ResponseEntity<List<AttendantResponseDTO>> listAll() {
        List<AttendantResponseDTO> attendants = attendantService.findAll();
        return ResponseEntity.ok(attendants);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an attendant")
    public ResponseEntity<AttendantResponseDTO> update(@PathVariable UUID id, @RequestBody AttendantRequestDTO attendantRequestDTO) {
        AttendantResponseDTO updatedAttendant = attendantService.update(id, attendantRequestDTO);
        return ResponseEntity.ok(updatedAttendant);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an attendant")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        attendantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
