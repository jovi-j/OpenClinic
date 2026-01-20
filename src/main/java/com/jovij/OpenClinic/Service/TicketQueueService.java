package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.InvalidCallToNextTicketException;
import com.jovij.OpenClinic.Exception.InvalidDateException;
import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Exception.TicketQueueAlreadyExistsException;
import com.jovij.OpenClinic.Model.Attendant;
import com.jovij.OpenClinic.Model.DTO.Ticket.TicketResponseDTO;
import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueCallRequestDTO;
import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueRequestDTO;
import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueResponseDTO;
import com.jovij.OpenClinic.Model.Enums.TicketStatus;
import com.jovij.OpenClinic.Model.Medic;
import com.jovij.OpenClinic.Model.Ticket;
import com.jovij.OpenClinic.Model.TicketQueue;
import com.jovij.OpenClinic.Repository.AttendantRepository;
import com.jovij.OpenClinic.Repository.MedicRepository;
import com.jovij.OpenClinic.Repository.TicketQueueRepository;
import com.jovij.OpenClinic.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketQueueService {

    private final TicketQueueRepository ticketQueueRepository;
    private final MedicRepository medicRepository;
    private final TicketRepository ticketRepository;
    private final AttendantRepository attendantRepository;

    @Autowired
    public TicketQueueService(TicketQueueRepository ticketQueueRepository, MedicRepository medicRepository, TicketRepository ticketRepository, AttendantRepository attendantRepository) {
        this.ticketQueueRepository = ticketQueueRepository;
        this.medicRepository = medicRepository;
        this.ticketRepository = ticketRepository;
        this.attendantRepository = attendantRepository;
    }

    @Transactional
    public TicketQueueResponseDTO create(TicketQueueRequestDTO ticketQueueRequestDTO) {
        LocalDate today = LocalDate.now();

        if (ticketQueueRepository.existsByMedicIdAndDate(ticketQueueRequestDTO.medicId(), today)) {
            throw new TicketQueueAlreadyExistsException("A ticket queue for this medic and date already exists.");
        }

        TicketQueue ticketQueue = new TicketQueue();
        ticketQueue.setDate(today);
        ticketQueue.setConsultationRoom(ticketQueueRequestDTO.consultationRoom());

        if (ticketQueueRequestDTO.medicId() != null) {
            Medic medic = medicRepository.findById(ticketQueueRequestDTO.medicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medic not found with id: " + ticketQueueRequestDTO.medicId()));
            ticketQueue.setMedic(medic);
        }

        TicketQueue savedTicketQueue = ticketQueueRepository.save(ticketQueue);
        return mapToDTO(savedTicketQueue);
    }

    public List<TicketQueueResponseDTO> findAll() {
        return ticketQueueRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketResponseDTO callNextTicket(TicketQueueCallRequestDTO ticketCall) {
        if(ticketCall.ticketQueueId() == null ||
            (ticketCall.attendantId() == null && ticketCall.medicId() == null)
        ){
            throw new InvalidCallToNextTicketException("Invalid call to next ticket.");
        }
        TicketQueue ticketQueue = ticketQueueRepository.findById(ticketCall.ticketQueueId())
                .orElseThrow(() -> new ResourceNotFoundException("TicketQueue not found"));

        if (!ticketQueue.getDate().isEqual(LocalDate.now())) {
            throw new InvalidDateException("Cannot call tickets from a closed queue (not today's date).");
        }


        boolean fromAttendant = ticketCall.isFromAttendant();

		List<Ticket> tickets = ticketRepository.findByTicketQueueIdAndStatusOrderByTicketPriorityDescCreatedAtAsc(
            ticketCall.ticketQueueId(),
            fromAttendant ? TicketStatus.WAITING_ATTENDANT : TicketStatus.WAITING_APPOINTMENT
        );

        if (tickets.isEmpty()) {
            throw new ResourceNotFoundException("No tickets waiting in this queue.");
        }

        Ticket nextTicket = tickets.get(0);

        if (fromAttendant) {
            Attendant attendant = attendantRepository.findById(ticketCall.attendantId())
                    .orElseThrow(() -> new ResourceNotFoundException("Attendant not found"));
            nextTicket.setAttendant(attendant);
            nextTicket.setStatus(TicketStatus.CALLED_BY_ATTENDANT);
        } else {
            nextTicket.setMedic(ticketQueue.getMedic());
            nextTicket.setStatus(TicketStatus.CALLED_BY_MEDIC);
        }

        ticketRepository.save(nextTicket);

        return mapToTicketDTO(nextTicket);
    }

    private TicketQueueResponseDTO mapToDTO(TicketQueue ticketQueue) {
        return new TicketQueueResponseDTO(
                ticketQueue.getId(),
                ticketQueue.getDate(),
                ticketQueue.getMedic() != null ? ticketQueue.getMedic().getId() : null,
                ticketQueue.getConsultationRoom()
        );
    }

    private TicketResponseDTO mapToTicketDTO(Ticket ticket) {
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
