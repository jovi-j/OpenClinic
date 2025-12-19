package com.jovij.OpenClinic.Model.DTO.Medic;

import com.jovij.OpenClinic.Model.DTO.Person.PersonResponseDTO;

import java.util.UUID;

public record MedicResponseDTO(UUID id, PersonResponseDTO person, String crm, String type) {
}
