package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Enums.TicketStatus;
import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class TicketAttendanceRecord extends GenericModel {
    private Ticket ticket;
    private Attendant attendant;
    private TicketStatus status;
}
