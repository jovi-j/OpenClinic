package com.jovij.OpenClinic.Model.DTO.Patient;

import com.jovij.OpenClinic.Model.DTO.Person.PersonResponseDTO;

import java.util.UUID;

public record PatientResponseDTO(UUID id, PersonResponseDTO person, String membershipId) {
}
