package com.andaluciaskills.andaluciasckills.Dto;

import lombok.Data;

@Data
public class DtoEvaluacion {
    private Integer idEvaluacion;
    private Integer prueba_idPrueba;
    private Integer participante_idParticipante;
    private Integer user_idUser;
    private Double notaFinal;
}