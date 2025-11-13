package com.jovij.OpenClinic.Model.DTO.Appointment;

import java.util.UUID;

public record ScheduleAppointmentDTO(UUID patientId, UUID appointmentId) {
}
