package com.andaluciaskills.andaluciasckills.Dto;

import lombok.Data;

@Data
public class DtoPrueba {
    private Integer idPrueba;
    private String enunciado;
    private Integer puntuacionMaxima;
    private Integer especialidad_idEspecialidad;
    private Integer participante_idParticipante;
    private String nombreUser;
    private String apellidosUser;
}
