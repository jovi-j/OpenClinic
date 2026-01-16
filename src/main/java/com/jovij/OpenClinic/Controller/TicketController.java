package com.jovij.OpenClinic.Controller;

import com.jovij.OpenClinic.Model.DTO.Ticket.TicketRedirectDTO;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketResponseDTO;
import com.jovij.OpenClinic.Service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/tickets", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tickets", description = "Endpoints for managing tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new ticket", description = "Creates a new ticket for a specific queue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Ticket queue not found")
    })
    public ResponseEntity<TicketResponseDTO> create(@RequestBody TicketRequestDTO ticketRequestDTO) {
        TicketResponseDTO createdTicket = ticketService.create(ticketRequestDTO);
        URI location = URI.create("/tickets/" + createdTicket.id());
        return ResponseEntity.created(location).body(createdTicket);
    }

    @GetMapping
    @Operation(summary = "List all tickets", description = "Retrieves a list of all tickets.")
    @ApiResponse(responseCode = "200", description = "List of tickets retrieved successfully")
    public ResponseEntity<List<TicketResponseDTO>> listAll() {
        List<TicketResponseDTO> tickets = ticketService.findAll();
        return ResponseEntity.ok(tickets);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a ticket", description = "Updates an existing ticket's information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket updated successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TicketResponseDTO> update(
            @Parameter(description = "ID of the ticket to update") @PathVariable UUID id,
            @RequestBody TicketRequestDTO ticketRequestDTO) {
        TicketResponseDTO updatedTicket = ticketService.update(id, ticketRequestDTO);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a ticket", description = "Deletes a ticket by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the ticket to delete") @PathVariable UUID id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/redirect", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Redirect a ticket", description = "Redirects a ticket to a medic's queue if the patient has an appointment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket redirected successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket, Medic, Patient or Queue not found"),
            @ApiResponse(responseCode = "400", description = "Patient does not have an appointment with this medic today")
    })
    public ResponseEntity<TicketResponseDTO> redirectTicket(
            @Parameter(description = "ID of the ticket to redirect") @PathVariable UUID id,
            @RequestBody TicketRedirectDTO ticketRedirectDTO) {
        TicketResponseDTO redirectedTicket = ticketService.redirectTicket(id, ticketRedirectDTO);
        return ResponseEntity.ok(redirectedTicket);
    }

    @PostMapping(value = "/{id}/complete")
    @Operation(summary = "Complete a ticket", description = "Marks a ticket as served and the associated appointment as attended.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket completed successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "400", description = "Ticket must be associated with a medic and a patient")
    })
    public ResponseEntity<TicketResponseDTO> completeTicket(
            @Parameter(description = "ID of the ticket to complete") @PathVariable UUID id) {
        TicketResponseDTO completedTicket = ticketService.completeTicket(id);
        return ResponseEntity.ok(completedTicket);
    }

    @PostMapping(value = "/{id}/unredeemed")
    @Operation(summary = "Mark ticket as unredeemed", description = "Marks a ticket as unredeemed, removing it from the active queue.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket marked as unredeemed successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found")
    })
    public ResponseEntity<TicketResponseDTO> markAsUnredeemed(
            @Parameter(description = "ID of the ticket to mark as unredeemed") @PathVariable UUID id) {
        TicketResponseDTO unredeemedTicket = ticketService.markAsUnredeemed(id);
        return ResponseEntity.ok(unredeemedTicket);
    }
}
