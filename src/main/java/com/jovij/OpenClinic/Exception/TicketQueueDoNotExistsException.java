package com.jovij.OpenClinic.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TicketQueueDoNotExistsException extends RuntimeException {
    public TicketQueueDoNotExistsException(String message) {
        super(message);
    }
}
