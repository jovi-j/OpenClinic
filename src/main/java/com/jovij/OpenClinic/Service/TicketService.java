package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketResponseDTO;
import com.jovij.OpenClinic.Model.Enums.TicketStatus;
import com.jovij.OpenClinic.Model.Ticket;
import com.jovij.OpenClinic.Model.TicketQueue;
import com.jovij.OpenClinic.Repository.TicketQueueRepository;
import com.jovij.OpenClinic.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketQueueRepository ticketQueueRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TicketQueueRepository ticketQueueRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketQueueRepository = ticketQueueRepository;
    }

    @Transactional
    public TicketResponseDTO create(TicketRequestDTO ticketRequestDTO) {
        TicketQueue ticketQueue = ticketQueueRepository.findById(ticketRequestDTO.ticketQueueId())
                .orElseThrow(() -> new ResourceNotFoundException("TicketQueue not found with id: " + ticketRequestDTO.ticketQueueId()));

        int lastTicketNum = ticketQueue.getGeneratedTickets().size();

        Ticket ticket = new Ticket();
        ticket.setTicketQueue(ticketQueue);
        ticket.setTicketPriority(ticketRequestDTO.ticketPriority());
        ticket.setStatus(TicketStatus.WAITING_ATTENDANT);
        ticket.setTicketNum(lastTicketNum + 1);

        Ticket savedTicket = ticketRepository.save(ticket);
        return mapToDTO(savedTicket);
    }

    public List<TicketResponseDTO> findAll() {
        return ticketRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponseDTO update(UUID id, TicketRequestDTO ticketRequestDTO) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        if (ticketRequestDTO.ticketPriority() != null) {
            ticket.setTicketPriority(ticketRequestDTO.ticketPriority());
        }

        Ticket savedTicket = ticketRepository.save(ticket);
        return mapToDTO(savedTicket);
    }

    public void delete(UUID id) {
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
    }

    private TicketResponseDTO mapToDTO(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getTicketNum(),
                ticket.getTicketPriority(),
                ticket.getStatus(),
                ticket.getTicketQueue().getId()
        );
    }
}
