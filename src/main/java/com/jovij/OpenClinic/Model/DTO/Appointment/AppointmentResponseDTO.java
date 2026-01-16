package com.jovij.OpenClinic.Model.DTO.Appointment;

import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AppointmentResponseDTO(
        UUID id,
        LocalDate date,
        LocalTime time,
        AppointmentStatus status,
        UUID patientId,
        String patientName,
        UUID medicId,
        String medicName
) {
}
