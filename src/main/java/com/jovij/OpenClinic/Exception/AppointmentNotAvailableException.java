package com.jovij.OpenClinic.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AppointmentNotAvailableException extends RuntimeException {
    public AppointmentNotAvailableException(String message) {
        super(message);
    }
}
