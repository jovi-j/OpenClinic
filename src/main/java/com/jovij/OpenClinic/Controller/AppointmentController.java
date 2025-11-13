package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.Appointment;
import com.jovij.OpenClinic.Model.DTO.Appointment.ScheduleAppointmentDTO;
import com.jovij.OpenClinic.Service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/available/{medicId}")
    public ResponseEntity<List<Appointment>> getAvailableAppointments(@PathVariable UUID medicId) {
        List<Appointment> appointments = appointmentService.findAvailableAppointmentsByMedicId(medicId);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/schedule")
    public ResponseEntity<Appointment> schedule(@RequestBody ScheduleAppointmentDTO scheduleAppointmentDTO) {
        Appointment scheduledAppointment = appointmentService.schedule(scheduleAppointmentDTO);
        return ResponseEntity.ok(scheduledAppointment);
    }
}
