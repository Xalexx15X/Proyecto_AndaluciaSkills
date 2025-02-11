package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EvaluacionItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public EvaluacionItemNotFoundException(Integer id) {
        super("No se puede encontrar el ítem de evaluación con la ID: " + id);
    }
    
    public EvaluacionItemNotFoundException() {
        super("No se pueden encontrar ítems de evaluación");
    }
}
