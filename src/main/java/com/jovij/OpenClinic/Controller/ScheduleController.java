package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Schedule.ScheduleDTO;
import com.jovij.OpenClinic.Model.DTO.Schedule.ScheduleResponseDTO;
import com.jovij.OpenClinic.Service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@Tag(name = "Schedules", description = "Endpoints for managing schedules")
public class ScheduleController {
    @Autowired
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    @Operation(summary = "Create a new schedule")
    public ResponseEntity<ScheduleResponseDTO> create(@RequestBody ScheduleDTO dto) {
        ScheduleResponseDTO createdSchedule = scheduleService.create(dto);
        return ResponseEntity.ok(createdSchedule);
    }

}
