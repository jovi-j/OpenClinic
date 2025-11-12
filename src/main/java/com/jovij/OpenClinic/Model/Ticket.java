package com.jovij.OpenClinic.Model;

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
    private TicketQueue ticketQueue;

    @Override
    public String toString(){
        return this.ticketPriority.toString() + ticketNum.toString();
    }

    @Override
    public int compareTo(Ticket otherTicket) {
        //comparing the other ticket first to get the desired result of "higher priority = served first"
        int result = Integer.compare(otherTicket.getTicketPriority().ordinal(), this.getTicketPriority().ordinal());
        if (result == 0) {
            return Integer.compare(this.ticketNum, otherTicket.ticketNum);
        }
        return result;
    }
}
