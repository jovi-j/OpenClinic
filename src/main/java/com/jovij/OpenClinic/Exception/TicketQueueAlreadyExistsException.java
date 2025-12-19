package com.jovij.OpenClinic.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TicketQueueAlreadyExistsException extends RuntimeException {
    public TicketQueueAlreadyExistsException(String message) {
        super(message);
    }
}
