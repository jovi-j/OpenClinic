package com.jovij.OpenClinic.Model.DTO.TicketQueue;

import java.util.UUID;

public record TicketQueueRequestDTO(UUID medicId, Integer consultationRoom) {
}
