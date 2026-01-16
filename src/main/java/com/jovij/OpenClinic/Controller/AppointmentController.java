package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Appointment.AppointmentResponseDTO;
import com.jovij.OpenClinic.Model.DTO.Appointment.GroupedAppointmentsDTO;
import com.jovij.OpenClinic.Model.DTO.Appointment.ScheduleAppointmentDTO;
import com.jovij.OpenClinic.Model.DTO.Appointment.ScheduledAppointmentDTO;
import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import com.jovij.OpenClinic.Service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Appointments", description = "Endpoints for managing appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/availableByMedic/{medicId}")
    @Operation(summary = "Get available appointments by medic", description = "Retrieves a list of available appointments for a specific medic.")
    @ApiResponse(responseCode = "200", description = "List of available appointments retrieved successfully")
    public ResponseEntity<List<GroupedAppointmentsDTO>> getAvailableAppointments(
            @Parameter(description = "ID of the medic") @PathVariable UUID medicId) {
        List<GroupedAppointmentsDTO> appointments = appointmentService.findAvailableAppointmentsByMedicId(medicId);
        return ResponseEntity.ok(appointments);
    }

    @PatchMapping(value = "/scheduleAppointment", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Schedule an appointment", description = "Schedules an appointment for a patient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment scheduled successfully"),
            @ApiResponse(responseCode = "404", description = "Patient or Appointment not found"),
            @ApiResponse(responseCode = "409", description = "Appointment not available")
    })
    public ResponseEntity<ScheduledAppointmentDTO> scheduleAppointment(@RequestBody ScheduleAppointmentDTO scheduleAppointmentDTO) {
        ScheduledAppointmentDTO scheduledAppointment = appointmentService.schedule(scheduleAppointmentDTO);
        return ResponseEntity.ok(scheduledAppointment);
    }

    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel an appointment", description = "Cancels an existing appointment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Appointment cancelled successfully"),
            @ApiResponse(responseCode = "404", description = "Appointment not found"),
            @ApiResponse(responseCode = "409", description = "Cannot cancel an attended appointment")
    })
    public ResponseEntity<Void> cancelAppointment(
            @Parameter(description = "ID of the appointment to cancel") @PathVariable UUID id) {
        appointmentService.cancel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search appointments by criteria", description = "Searches for appointments based on various criteria with pagination.")
    @ApiResponse(responseCode = "200", description = "Page of appointments retrieved successfully")
    public ResponseEntity<Page<AppointmentResponseDTO>> searchAppointments(
            @Parameter(description = "ID of the patient") @RequestParam(required = false) UUID patientId,
            @Parameter(description = "Date of the appointment") @RequestParam(required = false) LocalDate date,
            @Parameter(description = "ID of the medic") @RequestParam(required = false) UUID medicId,
            @Parameter(description = "Status of the appointment") @RequestParam(required = false) AppointmentStatus status,
            @ParameterObject Pageable pageable
    ) {
        Page<AppointmentResponseDTO> appointments = appointmentService.searchAppointments(patientId, date, medicId, status, pageable);
        return ResponseEntity.ok(appointments);
    }
}
