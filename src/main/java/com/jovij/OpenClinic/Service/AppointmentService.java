package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.AppointmentNotAvailableException;
import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Model.Appointment;
import com.jovij.OpenClinic.Model.DTO.Appointment.AvailableAppointmentTimeDTO;
import com.jovij.OpenClinic.Model.DTO.Appointment.GroupedAppointmentsDTO;
import com.jovij.OpenClinic.Model.DTO.Appointment.ScheduleAppointmentDTO;
import com.jovij.OpenClinic.Model.DTO.Appointment.ScheduledAppointmentDTO;
import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import com.jovij.OpenClinic.Model.Patient;
import com.jovij.OpenClinic.Repository.AppointmentRepository;
import com.jovij.OpenClinic.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public List<GroupedAppointmentsDTO> findAvailableAppointmentsByMedicId(UUID medicId) {
        List<Appointment> appointments = appointmentRepository.findByStatusAndSchedule_Medic_Id(AppointmentStatus.OPEN, medicId);
        return appointments.stream()
                .collect(Collectors.groupingBy(Appointment::getDate))
                .entrySet().stream()
                .map(entry -> new GroupedAppointmentsDTO(
                        entry.getKey(),
                        entry.getValue().stream()
                                .map(app -> new AvailableAppointmentTimeDTO(app.getId(), app.getTime().getHour(), app.getTime().getMinute()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public ScheduledAppointmentDTO schedule(ScheduleAppointmentDTO scheduleAppointmentDTO) {
        Patient patient = patientRepository.findById(scheduleAppointmentDTO.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        Appointment appointment = appointmentRepository.findById(scheduleAppointmentDTO.appointmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.OPEN) {
            throw new AppointmentNotAvailableException("Appointment is not available");
        }

        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        return new ScheduledAppointmentDTO(
                savedAppointment.getDate(),
                savedAppointment.getTime().getHour(),
                savedAppointment.getTime().getMinute()
        );
    }
}
