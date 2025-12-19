package com.jovij.OpenClinic.Model.DTO.Person;

import java.util.UUID;

public record PatientResponseDTO(UUID id, PersonResponseDTO person, String membershipId) {
}
