package com.jovij.OpenClinic.Model.DTO.Attendant;

import com.jovij.OpenClinic.Model.DTO.Person.PersonResponseDTO;

import java.util.UUID;

public record AttendantResponseDTO(UUID id, PersonResponseDTO person, int ticketWindow) {
}
