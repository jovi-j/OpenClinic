package com.jovij.OpenClinic.Model.DTO.Attendant;

import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;

import java.util.UUID;

public record AttendantResponseDTO(UUID id, PersonDTO person, int ticketWindow) {
}
