package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Model.Appointment;
import com.jovij.OpenClinic.Model.DTO.ScheduleDTO;
import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import com.jovij.OpenClinic.Model.Medic;
import com.jovij.OpenClinic.Model.Schedule;
import com.jovij.OpenClinic.Repository.MedicRepository;
import com.jovij.OpenClinic.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MedicRepository medicRepository;

    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository, MedicRepository medicRepository) {
        this.scheduleRepository = scheduleRepository;
        this.medicRepository = medicRepository;
    }

    @Transactional
    public Schedule create(ScheduleDTO scheduleDTO) {
        Medic medic = medicRepository.findByName(scheduleDTO.medicName())
                .orElseThrow(() -> new RuntimeException("Medic not found with name: " + scheduleDTO.medicName()));

        Schedule schedule = new Schedule();
        schedule.setMedic(medic);
        schedule.setMonth(scheduleDTO.month());
        schedule.setYear(scheduleDTO.year());

        List<Appointment> appointments = generateAppointmentsForMonth(scheduleDTO, schedule);
        schedule.setAppointments(appointments);

        return scheduleRepository.save(schedule);
    }

    private List<Appointment> generateAppointmentsForMonth(ScheduleDTO scheduleDTO, Schedule schedule) {
        List<Appointment> appointments = new ArrayList<>();
        LocalDate date = LocalDate.of(scheduleDTO.year().getValue(), scheduleDTO.month(), 1);
        int daysInMonth = date.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = LocalDate.of(scheduleDTO.year().getValue(), scheduleDTO.month(), day);
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                appointments.addAll(generateAppointmentsForDay(scheduleDTO, schedule, currentDate));
            }
        }
        return appointments;
    }

    private List<Appointment> generateAppointmentsForDay(ScheduleDTO scheduleDTO, Schedule schedule, LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        LocalTime currentTime = scheduleDTO.attendanceTimeStart();

        while (currentTime.isBefore(scheduleDTO.attendanceTimeEnd())) {
            if (scheduleDTO.lunchTimeStart() != null && scheduleDTO.lunchTimeEnd() != null &&
                    !currentTime.isBefore(scheduleDTO.lunchTimeStart()) && currentTime.isBefore(scheduleDTO.lunchTimeEnd())) {
                currentTime = scheduleDTO.lunchTimeEnd();
                continue;
            }

            Appointment appointment = new Appointment();
            appointment.setSchedule(schedule);
            appointment.setDate(date);
            appointment.setTime(currentTime);
            appointment.setStatus(AppointmentStatus.OPEN);
            appointments.add(appointment);

            currentTime = currentTime.plusMinutes(scheduleDTO.averageTimeForAppointment());
        }
        return appointments;
    }
}
