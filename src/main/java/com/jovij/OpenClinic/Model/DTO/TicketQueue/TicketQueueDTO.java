package com.jovij.OpenClinic.Model.DTO.TicketQueue;

import java.time.LocalDate;
import java.util.UUID;

public record TicketQueueDTO(LocalDate date, UUID medicId) {
}
