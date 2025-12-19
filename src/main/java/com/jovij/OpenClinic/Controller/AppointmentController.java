package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Appointment.GroupedAppointmentsDTO;
import com.jovij.OpenClinic.Model.DTO.Appointment.ScheduleAppointmentDTO;
import com.jovij.OpenClinic.Model.DTO.Appointment.ScheduledAppointmentDTO;
import com.jovij.OpenClinic.Service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/appointments")
@Tag(name = "Appointments", description = "Endpoints for managing appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/availableByMedic/{medicId}")
    @Operation(summary = "Get available appointments by medic")
    public ResponseEntity<List<GroupedAppointmentsDTO>> getAvailableAppointments(@PathVariable UUID medicId) {
        List<GroupedAppointmentsDTO> appointments = appointmentService.findAvailableAppointmentsByMedicId(medicId);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping("/scheduleAppointment")
    @Operation(summary = "Schedule an appointment")
    public ResponseEntity<ScheduledAppointmentDTO> scheduleAppointment(@RequestBody ScheduleAppointmentDTO scheduleAppointmentDTO) {
        ScheduledAppointmentDTO scheduledAppointment = appointmentService.schedule(scheduleAppointmentDTO);
        return ResponseEntity.ok(scheduledAppointment);
    }
}
