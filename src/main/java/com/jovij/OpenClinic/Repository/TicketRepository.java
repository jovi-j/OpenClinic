package com.jovij.OpenClinic.Repository;

import com.jovij.OpenClinic.Model.Enums.TicketStatus;
import com.jovij.OpenClinic.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByTicketQueueIdAndStatusOrderByTicketPriorityDescCreatedAtAsc(UUID ticketQueueId, TicketStatus status);

    @Query("select COALESCE(MAX(t.ticketNum), 0) from Ticket t where CAST(t.createdAt as date) = CURRENT_DATE()")
    Integer findLastTicketNumToday();
}
