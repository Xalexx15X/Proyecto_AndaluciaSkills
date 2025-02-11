package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EvaluacionNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public EvaluacionNotFoundException(Integer id) {
        super("No se puede encontrar la evaluación con la ID: " + id);
    }
    
    public EvaluacionNotFoundException() {
        super("No se pueden encontrar evaluaciones");
    }
}
