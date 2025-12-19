package com.jovij.OpenClinic.Repository;

import com.jovij.OpenClinic.Model.TicketQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface TicketQueueRepository extends JpaRepository<TicketQueue, UUID> {
    boolean existsByMedicIdAndDate(UUID uuid, LocalDate date);
}
