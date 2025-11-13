package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Schedule.ScheduleDTO;
import com.jovij.OpenClinic.Model.Schedule;
import com.jovij.OpenClinic.Service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {
    @Autowired
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<Schedule> create(@RequestBody ScheduleDTO dto) {
        Schedule createdSchedule = scheduleService.create(dto);
        return ResponseEntity.ok(createdSchedule);
    }

}
