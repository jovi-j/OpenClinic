package com.jovij.OpenClinic.Model.DTO.Attendant;

import com.jovij.OpenClinic.Model.DTO.Person.PersonDTO;

public record AttendantDTO(PersonDTO person, int ticketWindow) {
}
