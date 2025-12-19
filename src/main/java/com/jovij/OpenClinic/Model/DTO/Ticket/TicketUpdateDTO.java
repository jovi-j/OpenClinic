package com.jovij.OpenClinic.Model.DTO.Ticket;

import com.jovij.OpenClinic.Model.Enums.TicketPriority;

public record TicketUpdateDTO(TicketPriority ticketPriority) {
}
