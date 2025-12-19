package com.jovij.OpenClinic.Model.DTO.TicketQueue;

import java.time.LocalDate;
import java.util.UUID;

public record TicketQueueRequestDTO(LocalDate date, UUID medicId) {
}
