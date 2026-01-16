package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Model.*;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketRedirectDTO;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketRequestDTO;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketResponseDTO;
import com.jovij.OpenClinic.Model.Enums.AppointmentStatus;
import com.jovij.OpenClinic.Model.Enums.TicketStatus;
import com.jovij.OpenClinic.Repository.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketQueueRepository ticketQueueRepository;
    private final MedicRepository medicRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentService appointmentService;

    @Autowired
    public TicketService(TicketRepository ticketRepository, TicketQueueRepository ticketQueueRepository, MedicRepository medicRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository, AppointmentService appointmentService) {
        this.ticketRepository = ticketRepository;
        this.ticketQueueRepository = ticketQueueRepository;
        this.medicRepository = medicRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentService = appointmentService;
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

    @Transactional
    public TicketResponseDTO redirectTicket(UUID ticketId, TicketRedirectDTO ticketRedirectDTO) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        Medic medic = medicRepository.findById(ticketRedirectDTO.medicId())
                .orElseThrow(() -> new ResourceNotFoundException("Medic not found"));

        Patient patient = patientRepository.findById(ticketRedirectDTO.patientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        // Check if patient has an appointment with the medic today
        Specification<Appointment> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.equal(root.get("patient").get("id"), patient.getId()));
            predicates.add(criteriaBuilder.equal(root.get("date"), LocalDate.now()));
            predicates.add(criteriaBuilder.equal(root.get("schedule").get("medic").get("id"), medic.getId()));
            predicates.add(criteriaBuilder.equal(root.get("status"), AppointmentStatus.SCHEDULED));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        List<Appointment> appointments = appointmentRepository.findAll(spec);

        if (appointments.isEmpty()) {
            throw new IllegalArgumentException("Patient does not have a scheduled appointment with this medic today.");
        }

        // Find medic's queue for today
        TicketQueue medicQueue = ticketQueueRepository.findByMedicIdAndDate(medic.getId(), LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException("Medic does not have an active queue for today."));

        // Redirect ticket
        ticket.setTicketQueue(medicQueue);
        ticket.setPatient(patient);
        ticket.setStatus(TicketStatus.WAITING_ATTENDANT);

        Ticket savedTicket = ticketRepository.save(ticket);
        return mapToDTO(savedTicket);
    }

    @Transactional
    public TicketResponseDTO completeTicket(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        if (ticket.getMedic() == null || ticket.getPatient() == null) {
            throw new IllegalArgumentException("Ticket must be associated with a medic and a patient to be completed.");
        }

        // Update ticket status
        ticket.setStatus(TicketStatus.SERVED);
        Ticket savedTicket = ticketRepository.save(ticket);

        // Update appointment status via AppointmentService
        appointmentService.completeAppointment(ticket.getPatient().getId(), ticket.getMedic().getId());

        return mapToDTO(savedTicket);
    }

    @Transactional
    public TicketResponseDTO markAsUnredeemed(UUID ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));

        ticket.setStatus(TicketStatus.UNREDEEMED);
        Ticket savedTicket = ticketRepository.save(ticket);
        return mapToDTO(savedTicket);
    }

    private TicketResponseDTO mapToDTO(Ticket ticket) {
        return new TicketResponseDTO(
                ticket.getId(),
                ticket.getTicketNum(),
                ticket.getTicketPriority(),
                ticket.getStatus(),
                ticket.getTicketQueue().getId(),
                ticket.getMedic() != null ? ticket.getMedic().getId() : null,
                ticket.getAttendant() != null ? ticket.getAttendant().getId() : null,
                ticket.getPatient() != null ? ticket.getPatient().getId() : null
        );
    }
}
