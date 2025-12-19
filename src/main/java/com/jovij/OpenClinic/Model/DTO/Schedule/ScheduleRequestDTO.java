package com.jovij.OpenClinic.Model.DTO.Schedule;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ScheduleRequestDTO(
        UUID medicId,
        @Schema(description = "Month number, 1=January, 12=December")
        int month,
        int year,
        int attendanceHourStart,
        int attendanceMinuteStart,
        int attendanceHourEnd,
        int attendanceMinuteEnd,
        int lunchHourStart,
        int lunchMinuteStart,
        int lunchHourEnd,
        int lunchMinuteEnd,
        int averageTimeForAppointment
) {
}
