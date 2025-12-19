package com.jovij.OpenClinic.Model.DTO.Medic;

import com.jovij.OpenClinic.Model.DTO.Person.PersonUpdateDTO;

public record MedicUpdateDTO(PersonUpdateDTO person, String crm, String type) {
}
