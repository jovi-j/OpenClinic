package com.jovij.OpenClinic.Model;

import com.jovij.OpenClinic.Model.Generics.GenericModel;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.PriorityQueue;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class TicketQueue extends GenericModel {
    PriorityQueue<Ticket> generatedTickets;

}
