package com.jovij.OpenClinic.Model.DTO.Attendant;

import com.jovij.OpenClinic.Model.DTO.Person.PersonRequestDTO;

public record AttendantRequestDTO(PersonRequestDTO person, Integer ticketWindow) {
}
