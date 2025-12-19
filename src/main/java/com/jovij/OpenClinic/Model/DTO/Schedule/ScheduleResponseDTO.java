package com.jovij.OpenClinic.Model.DTO.Schedule;

import java.time.Month;
import java.time.Year;
import java.util.UUID;

public record ScheduleResponseDTO(UUID id, UUID medicId, Month month, Year year, int numberOfAppointments) {
}
