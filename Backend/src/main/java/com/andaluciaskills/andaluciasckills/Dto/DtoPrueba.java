package com.andaluciaskills.andaluciasckills.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DtoPrueba {

    private Integer idPrueba;
    private String enunciado;
    private Integer puntuacionMaxima;

    @JsonProperty("Especialidad_idEspecialidad")
    private Integer especialidad_idEspecialidad;

}
