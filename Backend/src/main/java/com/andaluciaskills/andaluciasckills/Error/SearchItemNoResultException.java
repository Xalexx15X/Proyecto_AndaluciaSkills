package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SearchItemNoResultException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public SearchItemNoResultException() {
        super("No se encontraron resultados para la búsqueda de ítems");
    }
}
