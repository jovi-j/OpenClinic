package com.jovij.OpenClinic.Service;

import com.jovij.OpenClinic.Exception.ResourceNotFoundException;
import com.jovij.OpenClinic.Exception.TicketQueueAlreadyExistsException;
import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueDTO;
import com.jovij.OpenClinic.Model.DTO.TicketQueue.TicketQueueResponseDTO;
import com.jovij.OpenClinic.Model.Medic;
import com.jovij.OpenClinic.Model.TicketQueue;
import com.jovij.OpenClinic.Repository.MedicRepository;
import com.jovij.OpenClinic.Repository.TicketQueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketQueueService {

    private final TicketQueueRepository ticketQueueRepository;
    private final MedicRepository medicRepository;

    @Autowired
    public TicketQueueService(TicketQueueRepository ticketQueueRepository, MedicRepository medicRepository) {
        this.ticketQueueRepository = ticketQueueRepository;
        this.medicRepository = medicRepository;
    }

    @Transactional
    public TicketQueueResponseDTO create(TicketQueueDTO ticketQueueDTO) {
        if (ticketQueueRepository.existsByMedicIdAndDate(ticketQueueDTO.medicId(), ticketQueueDTO.date())) {
            throw new TicketQueueAlreadyExistsException("A ticket queue for this medic and date already exists.");
        }

        TicketQueue ticketQueue = new TicketQueue();
        ticketQueue.setDate(ticketQueueDTO.date());

        if (ticketQueueDTO.medicId() != null) {
            Medic medic = medicRepository.findById(ticketQueueDTO.medicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Medic not found with id: " + ticketQueueDTO.medicId()));
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

    private TicketQueueResponseDTO mapToDTO(TicketQueue ticketQueue) {
        return new TicketQueueResponseDTO(
                ticketQueue.getId(),
                ticketQueue.getDate(),
                ticketQueue.getMedic() != null ? ticketQueue.getMedic().getId() : null
        );
    }
}
