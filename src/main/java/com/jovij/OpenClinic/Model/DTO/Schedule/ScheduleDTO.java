package com.jovij.OpenClinic.Model.DTO.Schedule;

import java.time.LocalTime;
import java.time.Month;
import java.time.Year;

public record ScheduleDTO(
        String medicName,
        Month month,
        Year year,
        LocalTime attendanceTimeStart,
        LocalTime attendanceTimeEnd,
        LocalTime lunchTimeStart,
        LocalTime lunchTimeEnd,
        int averageTimeForAppointment
) {
}
