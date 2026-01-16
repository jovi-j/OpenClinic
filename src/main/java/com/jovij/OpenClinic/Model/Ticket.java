package com.jovij.OpenClinic.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jovij.OpenClinic.Model.Enums.TicketPriority;
import com.jovij.OpenClinic.Model.Enums.TicketStatus;
import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Ticket extends GenericModel implements Comparable<Ticket> {
    private Integer ticketNum;

    private TicketPriority ticketPriority;
    private TicketStatus status;

    @ManyToOne
    @JsonIgnore
    private TicketQueue ticketQueue;

    @ManyToOne
    private Medic medic;

    @ManyToOne
    private Attendant attendant;

    @ManyToOne
    private Patient patient;

    @Override
    public String toString(){
        return (this.ticketPriority != null ? this.ticketPriority.toString() : "NULL") + ticketNum.toString();
    }

    @Override
    public int compareTo(Ticket otherTicket) {
        if (otherTicket == null) {
            return 1;
        }
        if (this.ticketPriority == null && otherTicket.getTicketPriority() == null) {
            return Integer.compare(this.ticketNum, otherTicket.ticketNum);
        }
        if (this.ticketPriority == null) {
            return -1; // Null priority is lower than any priority
        }
        if (otherTicket.getTicketPriority() == null) {
            return 1; // Any priority is higher than null
        }

        //comparing the other ticket first to get the desired result of "higher priority = served first"
        int result = Integer.compare(otherTicket.getTicketPriority().ordinal(), this.getTicketPriority().ordinal());
        if (result == 0) {
            return Integer.compare(this.ticketNum, otherTicket.ticketNum);
        }
        return result;
    }
}
