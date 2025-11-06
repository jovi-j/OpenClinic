package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Appointment extends GenericModel {
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;

    @OneToOne
    private Patient patient;

    @ManyToOne(optional = false)
    private Schedule schedule;

}
