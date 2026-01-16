package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Exception.ScheduleAlreadyExistsException;
import com.jovij.OpenClinic.Model.Appointment;
import com.jovij.OpenClinic.Model.DTO.Schedule.ScheduleRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Schedule.ScheduleResponseDTO;
import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import com.jovij.OpenClinic.Model.Medic;
import com.jovij.OpenClinic.Model.Schedule;
import com.jovij.OpenClinic.Repository.MedicRepository;
import com.jovij.OpenClinic.Repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
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
    public ScheduleResponseDTO create(ScheduleRequestDTO scheduleRequestDTO) {
        Medic medic = medicRepository.findById(scheduleRequestDTO.medicId())
                .orElseThrow(() -> new ResourceNotFoundException("Medic not found with id: " + scheduleRequestDTO.medicId()));

        if (scheduleRepository.existsByMedicIdAndMonthAndYear(medic.getId(), Month.of(scheduleRequestDTO.month()), Year.of(scheduleRequestDTO.year()))) {
            throw new ScheduleAlreadyExistsException("The medic already has a schedule for this month and year.");
        }

        Schedule schedule = new Schedule();
        schedule.setMedic(medic);
        schedule.setMonth(Month.of(scheduleRequestDTO.month()));
        schedule.setYear(Year.of(scheduleRequestDTO.year()));

        List<Appointment> appointments = generateAppointmentsForMonth(scheduleRequestDTO, schedule);
        schedule.setAppointments(appointments);

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return mapToDTO(savedSchedule);
    }

    private List<Appointment> generateAppointmentsForMonth(ScheduleRequestDTO scheduleRequestDTO, Schedule schedule) {
        List<Appointment> appointments = new ArrayList<>();
        LocalDate date = LocalDate.of(schedule.getYear().getValue(), schedule.getMonth(), 1);
        int daysInMonth = date.lengthOfMonth();

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = LocalDate.of(schedule.getYear().getValue(), schedule.getMonth(), day);
            if (currentDate.getDayOfWeek() != DayOfWeek.SATURDAY && currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                appointments.addAll(generateAppointmentsForDay(scheduleRequestDTO, schedule, currentDate));
            }
        }
        return appointments;
    }

    private List<Appointment> generateAppointmentsForDay(ScheduleRequestDTO scheduleRequestDTO, Schedule schedule, LocalDate date) {
        List<Appointment> appointments = new ArrayList<>();
        LocalTime currentTime = LocalTime.of(scheduleRequestDTO.attendanceHourStart(), scheduleRequestDTO.attendanceMinuteStart());
        LocalTime attendanceTimeEnd = LocalTime.of(scheduleRequestDTO.attendanceHourEnd(), scheduleRequestDTO.attendanceMinuteEnd());
        LocalTime lunchTimeStart = LocalTime.of(scheduleRequestDTO.lunchHourStart(), scheduleRequestDTO.lunchMinuteStart());
        LocalTime lunchTimeEnd = LocalTime.of(scheduleRequestDTO.lunchHourEnd(), scheduleRequestDTO.lunchMinuteEnd());


        while (currentTime.isBefore(attendanceTimeEnd)) {
            if (!currentTime.isBefore(lunchTimeStart) && currentTime.isBefore(lunchTimeEnd)) {
                currentTime = lunchTimeEnd;
                continue;
            }

            Appointment appointment = new Appointment();
            appointment.setSchedule(schedule);
            appointment.setDate(date);
            appointment.setTime(currentTime);
            appointment.setStatus(AppointmentStatus.OPEN);
            appointments.add(appointment);

            currentTime = currentTime.plusMinutes(scheduleRequestDTO.averageTimeForAppointment());
        }
        return appointments;
    }

    private ScheduleResponseDTO mapToDTO(Schedule schedule) {
        return new ScheduleResponseDTO(
                schedule.getId(),
                schedule.getMedic().getId(),
                schedule.getMonth(),
                schedule.getYear(),
                schedule.getAppointments().size()
        );
    }
}
