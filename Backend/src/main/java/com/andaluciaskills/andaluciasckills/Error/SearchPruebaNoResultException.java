package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SearchPruebaNoResultException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public SearchPruebaNoResultException() {
        super("No se encontraron resultados para la búsqueda de pruebas");
    }
}