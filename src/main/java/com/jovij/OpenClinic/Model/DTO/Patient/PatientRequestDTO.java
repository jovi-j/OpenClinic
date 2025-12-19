package com.jovij.OpenClinic.Model.DTO.Patient;

import com.jovij.OpenClinic.Model.DTO.Person.PersonRequestDTO;

public record PatientRequestDTO(PersonRequestDTO person, String membershipId) {
}
