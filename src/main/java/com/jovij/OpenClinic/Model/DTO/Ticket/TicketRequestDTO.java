package com.jovij.OpenClinic.Model.DTO.Ticket;

import com.jovij.OpenClinic.Model.Enums.TicketPriority;

import java.util.UUID;

public record TicketRequestDTO(UUID ticketQueueId, TicketPriority ticketPriority) {
}
