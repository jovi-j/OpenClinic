package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Patient extends GenericModel {
    @Column(nullable = false, unique = true)
    private String membershipId;

    @OneToOne
    @Column(nullable = false)
    private Person person;
}
