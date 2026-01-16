package com.jovij.OpenClinic.Model.Enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketPriority {
    NORMAL("NMT"),
    EXAM_RESULTS("ERT"),
    PREFERENTIAL("PRT");

    private final String acronym;

    TicketPriority(String acronym) {
        this.acronym = acronym;
    }

    @JsonCreator
    public static TicketPriority fromAcronym(String acronym) {
        for (TicketPriority priority : TicketPriority.values()) {
            if (priority.acronym.equalsIgnoreCase(acronym)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Unknown enum type " + acronym + ", Allowed values are " + java.util.Arrays.toString(values()));
    }

    @JsonValue
    public String getAcronym() {
        return acronym;
    }

    @Override
    public String toString() {
        return acronym;
    }
}
