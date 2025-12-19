package com.jovij.OpenClinic.Model.DTO.Medic;

import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;

import java.util.UUID;

public record MedicResponseDTO(UUID id, PersonDTO person, String crm, String type) {
}
