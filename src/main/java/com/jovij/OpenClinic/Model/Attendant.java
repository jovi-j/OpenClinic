package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Attendant extends GenericModel {
    private int ticketWindow;

    @OneToOne
    private Person person;
}
