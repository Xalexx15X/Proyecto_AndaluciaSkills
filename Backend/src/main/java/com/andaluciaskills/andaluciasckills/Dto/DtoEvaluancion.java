package com.andaluciaskills.andaluciasckills.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DtoEvaluancion {
    private Integer idEvaluacion;
    private Double notaFinal;
    @JsonProperty("Participante_idParticipante")
    private Integer participante_idParticipante;
    @JsonProperty("User_idUser")
    private Integer user_idUser;
    @JsonProperty("Prueba_idPrueba")
    private Integer prueba_idPrueba;
}
