package com.jovij.OpenClinic.Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCallToNextTicketException extends RuntimeException {
    public InvalidCallToNextTicketException(String message) {
        super(message);
    }
}
