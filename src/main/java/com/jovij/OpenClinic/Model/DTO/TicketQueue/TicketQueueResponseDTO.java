package com.jovij.OpenClinic.Model.DTO.TicketQueue;

import java.time.LocalDate;
import java.util.UUID;

public record TicketQueueResponseDTO(UUID id, LocalDate date, UUID medicId, Integer consultationRoom) {
}
