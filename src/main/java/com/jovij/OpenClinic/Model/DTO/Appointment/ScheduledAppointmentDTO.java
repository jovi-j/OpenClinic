package com.jovij.OpenClinic.Model.DTO.Appointment;

import java.time.LocalDate;

public record ScheduledAppointmentDTO(LocalDate date, int hour, int minute) {
}
