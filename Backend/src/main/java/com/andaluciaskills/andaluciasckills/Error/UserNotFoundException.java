package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public UserNotFoundException(Integer id) {
        super("No se puede encontrar el usuario con la ID: " + id);
    }
    
    public UserNotFoundException() {
        super("No se pueden encontrar usuarios");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
