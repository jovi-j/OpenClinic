package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Model.Appointment;
import com.jovij.OpenClinic.Model.DTO.Appointment.ScheduleAppointmentDTO;
import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import com.jovij.OpenClinic.Model.Patient;
import com.jovij.OpenClinic.Repository.AppointmentRepository;
import com.jovij.OpenClinic.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository, PatientRepository patientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
    }

    public List<Appointment> findAvailableAppointmentsByMedicId(UUID medicId) {
        return appointmentRepository.findByStatusAndSchedule_Medic_Id(AppointmentStatus.OPEN, medicId);
    }

    @Transactional
    public Appointment schedule(ScheduleAppointmentDTO scheduleAppointmentDTO) {
        Patient patient = patientRepository.findById(scheduleAppointmentDTO.patientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Appointment appointment = appointmentRepository.findById(scheduleAppointmentDTO.appointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != AppointmentStatus.OPEN) {
            throw new RuntimeException("Appointment is not available");
        }

        appointment.setPatient(patient);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        return appointmentRepository.save(appointment);
    }
}
