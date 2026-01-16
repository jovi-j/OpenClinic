package com.jovij.OpenClinic.Model.DTO.Ticket;

import java.util.UUID;

public record TicketRedirectDTO(UUID medicId, UUID patientId) {
}
