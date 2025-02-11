package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EspecialidadBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public EspecialidadBadRequestException(String mensaje) {
        super(mensaje);
    }
}