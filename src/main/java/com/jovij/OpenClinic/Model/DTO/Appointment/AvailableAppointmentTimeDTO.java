package com.jovij.OpenClinic.Model.DTO.Appointment;

import java.util.UUID;

public record AvailableAppointmentTimeDTO(UUID id, int hour, int minute) {
}
