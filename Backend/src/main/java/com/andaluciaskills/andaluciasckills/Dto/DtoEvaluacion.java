package com.andaluciaskills.andaluciasckills.Dto;

import lombok.Data;

@Data
public class DtoEvaluacion {
    private Integer idEvaluacion;
    private Double notaFinal;
    private Integer participante_idParticipante;
    private Integer prueba_idPrueba;
    private Integer user_idUser;
}