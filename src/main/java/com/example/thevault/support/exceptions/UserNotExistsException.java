package com.example.thevault.support.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Gebruiker bestaat niet.")
public class UserNotExistsException extends RuntimeException {

    public UserNotExistsException() {
        super();
    }

}
