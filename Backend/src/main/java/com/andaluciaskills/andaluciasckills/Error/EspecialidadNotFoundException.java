package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EspecialidadNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public EspecialidadNotFoundException(Integer id) {
        super("No se puede encontrar la especialidad con la ID: ");
    }
    
    public EspecialidadNotFoundException() {
        super("No se pueden encontrar especialidades");
    }
}