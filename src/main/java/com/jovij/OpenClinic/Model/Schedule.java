package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Month;
import java.time.Year;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Schedule extends GenericModel {
    private Month month;
    private Year year;

    @OneToOne
    private Medic medic;

    @OneToMany
    private List<Appointment> appointments;

}
