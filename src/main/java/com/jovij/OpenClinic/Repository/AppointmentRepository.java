package com.jovij.OpenClinic.Repository;

import com.jovij.OpenClinic.Model.Appointment;
import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByStatusAndSchedule_Medic_Id(AppointmentStatus status, UUID medicId);
}
