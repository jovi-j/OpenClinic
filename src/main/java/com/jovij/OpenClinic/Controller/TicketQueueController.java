package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueDTO;
import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueResponseDTO;
import com.jovij.OpenClinic.Service.TicketQueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ticket-queues")
@Tag(name = "Ticket Queues", description = "Endpoints for managing ticket queues")
public class TicketQueueController {

    private final TicketQueueService ticketQueueService;

    @Autowired
    public TicketQueueController(TicketQueueService ticketQueueService) {
        this.ticketQueueService = ticketQueueService;
    }

    @PostMapping
    @Operation(summary = "Create a new ticket queue")
    public ResponseEntity<TicketQueueResponseDTO> create(@RequestBody TicketQueueDTO ticketQueueDTO) {
        TicketQueueResponseDTO createdTicketQueue = ticketQueueService.create(ticketQueueDTO);
        URI location = URI.create("/ticket-queues/" + createdTicketQueue.id());
        return ResponseEntity.created(location).body(createdTicketQueue);
    }

    @GetMapping
    @Operation(summary = "List all ticket queues")
    public ResponseEntity<List<TicketQueueResponseDTO>> listAll() {
        List<TicketQueueResponseDTO> ticketQueues = ticketQueueService.findAll();
        return ResponseEntity.ok(ticketQueues);
    }
}
