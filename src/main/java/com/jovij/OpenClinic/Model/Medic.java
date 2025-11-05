package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Medic extends GenericModel {
    private String crm;
    private String type;

    @OneToOne
    private Person person;
}
