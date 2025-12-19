package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Ticket.TicketCreateDTO;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketResponseDTO;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketUpdateDTO;
import com.jovij.OpenClinic.Service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@Tag(name = "Tickets", description = "Endpoints for managing tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @Operation(summary = "Create a new ticket")
    public ResponseEntity<TicketResponseDTO> create(@RequestBody TicketCreateDTO ticketCreateDTO) {
        TicketResponseDTO createdTicket = ticketService.create(ticketCreateDTO);
        URI location = URI.create("/tickets/" + createdTicket.id());
        return ResponseEntity.created(location).body(createdTicket);
    }

    @GetMapping
    @Operation(summary = "List all tickets")
    public ResponseEntity<List<TicketResponseDTO>> listAll() {
        List<TicketResponseDTO> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a ticket")
    public ResponseEntity<TicketResponseDTO> update(@PathVariable UUID id, @RequestBody TicketUpdateDTO ticketUpdateDTO) {
        TicketResponseDTO updatedTicket = ticketService.update(id, ticketUpdateDTO);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ticket")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
