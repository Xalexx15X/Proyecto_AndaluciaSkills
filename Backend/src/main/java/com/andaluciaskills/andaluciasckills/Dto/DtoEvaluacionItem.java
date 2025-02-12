package com.andaluciaskills.andaluciasckills.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DtoEvaluacionItem {
    private Integer idEvaluacionItem;
    private Integer valoracion;
    @JsonProperty("Evaluacion_idEvaluacion")
    private Integer Evaluacion_idEvaluacion;
    @JsonProperty("Item_idItem")
    private Integer Item_idItem;
    @JsonProperty("Prueba_idPrueba")
    private Integer Prueba_idPrueba;
}
