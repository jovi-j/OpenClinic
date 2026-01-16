package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.AppointmentNotAvailableException;
import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Model.Appointment;
import com.jovij.OpenClinic.Model.DTO.Appointment.*;
import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import com.jovij.OpenClinic.Model.Patient;
import com.jovij.OpenClinic.Repository.AppointmentRepository;
import com.jovij.OpenClinic.Repository.PatientRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Transactional
    public void cancel(UUID appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (appointment.getStatus() == AppointmentStatus.ATTENDED) {
            throw new IllegalStateException("Cannot cancel an attended appointment");
        }

        appointment.setPatient(null);
        appointment.setStatus(AppointmentStatus.OPEN);
        appointmentRepository.save(appointment);
    }

    @Transactional(readOnly = true)
    public Page<AppointmentResponseDTO> searchAppointments(UUID patientId, LocalDate date, UUID medicId, AppointmentStatus status, Pageable pageable) {
        Specification<Appointment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (patientId != null) {
                predicates.add(criteriaBuilder.equal(root.get("patient").get("id"), patientId));
            }
            if (date != null) {
                predicates.add(criteriaBuilder.equal(root.get("date"), date));
            }
            if (medicId != null) {
                predicates.add(criteriaBuilder.equal(root.get("schedule").get("medic").get("id"), medicId));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Appointment> appointmentsPage = appointmentRepository.findAll(spec, pageable);
        List<AppointmentResponseDTO> dtos = appointmentsPage.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, appointmentsPage.getTotalElements());
    }

    @Transactional
    public void completeAppointment(UUID patientId, UUID medicId) {
        Specification<Appointment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("patient").get("id"), patientId));
            predicates.add(criteriaBuilder.equal(root.get("date"), LocalDate.now()));
            predicates.add(criteriaBuilder.equal(root.get("schedule").get("medic").get("id"), medicId));
            predicates.add(criteriaBuilder.equal(root.get("status"), AppointmentStatus.SCHEDULED));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Appointment> appointments = appointmentRepository.findAll(spec);

        if (!appointments.isEmpty()) {
            Appointment appointment = appointments.get(0);
            appointment.setStatus(AppointmentStatus.ATTENDED);
            appointmentRepository.save(appointment);
        }
    }

    private AppointmentResponseDTO mapToResponseDTO(Appointment appointment) {
        return new AppointmentResponseDTO(
                appointment.getId(),
                appointment.getDate(),
                appointment.getTime(),
                appointment.getStatus(),
                appointment.getPatient() != null ? appointment.getPatient().getId() : null,
                appointment.getPatient() != null ? appointment.getPatient().getPerson().getName() : null,
                appointment.getSchedule().getMedic().getId(),
                appointment.getSchedule().getMedic().getPerson().getName()
        );
    }
}
