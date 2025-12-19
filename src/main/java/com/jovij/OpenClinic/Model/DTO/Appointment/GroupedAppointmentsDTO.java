package com.jovij.OpenClinic.Model.DTO.Appointment;

import java.time.LocalDate;
import java.util.List;

public record GroupedAppointmentsDTO(LocalDate date, List<AvailableAppointmentTimeDTO> appointments) {
}
