package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public ItemNotFoundException(Integer id) {
        super("No se puede encontrar el ítem con la ID: " + id);
    }
    
    public ItemNotFoundException() {
        super("No se pueden encontrar ítems");
    }
}
