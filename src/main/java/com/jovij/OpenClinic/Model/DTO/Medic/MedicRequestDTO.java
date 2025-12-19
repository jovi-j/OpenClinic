package com.jovij.OpenClinic.Model.DTO.Medic;

import com.jovij.OpenClinic.Model.DTO.Person.PersonRequestDTO;

public record MedicRequestDTO(PersonRequestDTO person, String crm, String type) {
}
