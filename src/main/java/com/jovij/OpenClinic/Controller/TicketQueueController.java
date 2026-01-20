package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Ticket.TicketResponseDTO;
import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueCallRequestDTO;
import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueRequestDTO;
import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueResponseDTO;
import com.jovij.OpenClinic.Service.TicketQueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/ticket-queues", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Ticket Queues", description = "Endpoints for managing ticket queues")
public class TicketQueueController {

    private final TicketQueueService ticketQueueService;

    @Autowired
    public TicketQueueController(TicketQueueService ticketQueueService) {
        this.ticketQueueService = ticketQueueService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new ticket queue", description = "Creates a new ticket queue for a specific date and optionally a medic.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket queue created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or date"),
            @ApiResponse(responseCode = "409", description = "Ticket queue already exists")
    })
    public ResponseEntity<TicketQueueResponseDTO> create(@RequestBody TicketQueueRequestDTO ticketQueueRequestDTO) {
        TicketQueueResponseDTO createdTicketQueue = ticketQueueService.create(ticketQueueRequestDTO);
        URI location = URI.create("/ticket-queues/" + createdTicketQueue.id());
        return ResponseEntity.created(location).body(createdTicketQueue);
    }

    @GetMapping
    @Operation(summary = "List all ticket queues", description = "Retrieves a list of all ticket queues.")
    @ApiResponse(responseCode = "200", description = "List of ticket queues retrieved successfully")
    public ResponseEntity<List<TicketQueueResponseDTO>> listAll() {
        List<TicketQueueResponseDTO> ticketQueues = ticketQueueService.findAll();
        return ResponseEntity.ok(ticketQueues);
    }

    @PostMapping("/call-next")
    @Operation(summary = "Call the next ticket in the queue", description = "Calls the next ticket in the specified queue based on priority and creation time.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Next ticket called successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket queue not found or no tickets in queue"),
            @ApiResponse(responseCode = "400", description = "Cannot call tickets from a closed queue")
    })
    public ResponseEntity<TicketResponseDTO> callNextTicket(@RequestBody TicketQueueCallRequestDTO ticketQueueCall) {
        TicketResponseDTO nextTicket = ticketQueueService.callNextTicket(ticketQueueCall);
        return ResponseEntity.ok(nextTicket);
    }
}
