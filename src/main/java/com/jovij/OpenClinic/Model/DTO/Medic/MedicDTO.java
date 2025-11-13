package com.jovij.OpenClinic.Model.DTO.Medic;

import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;

public record MedicDTO(PersonDTO person, String crm, String type) {
}
