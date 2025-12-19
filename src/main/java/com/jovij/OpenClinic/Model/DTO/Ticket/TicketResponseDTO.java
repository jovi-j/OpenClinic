package com.jovij.OpenClinic.Model.DTO.Ticket;

import com.jovij.OpenClinic.Model.Enums.TicketPriority;
import com.jovij.OpenClinic.Model.Enums.TicketStatus;

import java.util.UUID;

public record TicketResponseDTO(UUID id, int ticketNum, TicketPriority ticketPriority, TicketStatus status,
                                UUID ticketQueueId) {
}
