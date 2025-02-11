package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EvaluacionBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public EvaluacionBadRequestException(String mensaje) {
        super(mensaje);
    }
}