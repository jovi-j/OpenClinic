package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Schedule.ScheduleRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Schedule.ScheduleResponseDTO;
import com.jovij.OpenClinic.Service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Schedules", description = "Endpoints for managing schedules")
public class ScheduleController {
    @Autowired
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new schedule", description = "Creates a new schedule for a medic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Schedule created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Medic not found"),
            @ApiResponse(responseCode = "409", description = "Schedule already exists")
    })
    public ResponseEntity<ScheduleResponseDTO> create(@RequestBody ScheduleRequestDTO dto) {
        ScheduleResponseDTO createdSchedule = scheduleService.create(dto);
        return ResponseEntity.ok(createdSchedule);
    }

}
