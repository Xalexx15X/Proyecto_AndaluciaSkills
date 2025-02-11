package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PruebaNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public PruebaNotFoundException(Integer id) {
        super("No se puede encontrar la prueba con la ID: " + id);
    }
    
    public PruebaNotFoundException() {
        super("No se pueden encontrar pruebas");
    }
}