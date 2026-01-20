package com.jovij.OpenClinic.Model.DTO.TicketQueue;

import java.util.UUID;

public record TicketQueueCallRequestDTO(UUID ticketQueueId, UUID attendantId,UUID medicId) {
    public boolean isFromAttendant(){
        return attendantId != null;
    }
}
