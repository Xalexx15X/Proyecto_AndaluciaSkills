package com.andaluciaskills.andaluciasckills.Error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SearchUserNoResultException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public SearchUserNoResultException() {
        super("No se encontraron resultados para la búsqueda de usuarios");
    }
}
