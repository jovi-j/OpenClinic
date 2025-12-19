package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SQLDelete(sql = "UPDATE person SET deleted_at = now() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Person extends GenericModel {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false)
    private LocalDate birthDate;
}
