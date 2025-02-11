package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SearchEspecialidadNoResultException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public SearchEspecialidadNoResultException() {
        super("No se encontraron resultados para la búsqueda solicitada");
    }
}
