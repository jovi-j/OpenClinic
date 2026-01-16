package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Enums.TicketPriority;
import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"medic_id", "date"})
})
public class TicketQueue extends GenericModel {

    @OneToMany(mappedBy = "ticketQueue", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Ticket> generatedTickets = new ArrayList<>();

    @Transient
    private PriorityQueue<Ticket> ticketPriorityQueue = new PriorityQueue<>();

    @ManyToOne
    private Medic medic;

    private LocalDate date;

    private Integer consultationRoom;

    @PostLoad
    @PostPersist
    @PostUpdate
    public void initPriorityQueue() {
        this.ticketPriorityQueue = new PriorityQueue<>(this.generatedTickets);
    }

    public Ticket generateTicket(TicketPriority priority, int lastTicketNum) {
        Ticket ticket = new Ticket();
        ticket.setTicketNum(lastTicketNum + 1);
        ticket.setTicketPriority(priority);
        ticket.setTicketQueue(this);

        this.generatedTickets.add(ticket);
        this.ticketPriorityQueue.add(ticket);
        return ticket;
    }

    public Ticket callNextTicket() {
        Ticket nextTicket = this.ticketPriorityQueue.poll();
        if (nextTicket != null) {
            this.generatedTickets.remove(nextTicket);
        }
        return nextTicket;
    }
}
