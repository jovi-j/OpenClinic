package com.jovij.OpenClinic.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    private Patient patient;

    @ManyToOne(optional = false)
    @JsonIgnore
    private Schedule schedule;

}
