package com.jovij.OpenClinic.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ScheduleAlreadyExistsException extends RuntimeException {
    public ScheduleAlreadyExistsException(String message) {
        super(message);
    }
}
