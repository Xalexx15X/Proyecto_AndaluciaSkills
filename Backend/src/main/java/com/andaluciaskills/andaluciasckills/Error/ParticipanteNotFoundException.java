package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParticipanteNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public ParticipanteNotFoundException(Integer id) {
        super("No se puede encontrar el participante con la ID: " + id);
    }
    
    public ParticipanteNotFoundException() {
        super("No se pueden encontrar participantes");
    }
}
