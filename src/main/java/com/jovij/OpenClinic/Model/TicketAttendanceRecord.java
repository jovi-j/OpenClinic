package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Enums.TicketStatus;
import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class TicketAttendanceRecord extends GenericModel {
    @ManyToOne
    private Ticket ticket;

    @ManyToOne
    private Attendant attendant;
    
    private TicketStatus status;
}
