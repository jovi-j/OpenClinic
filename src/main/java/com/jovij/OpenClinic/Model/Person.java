package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Person extends GenericModel {
    private String name;
    private String cpf;
    private LocalDate birthDate;
}
