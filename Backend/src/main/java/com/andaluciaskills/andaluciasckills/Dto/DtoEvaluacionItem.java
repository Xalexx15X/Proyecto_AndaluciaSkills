package com.andaluciaskills.andaluciasckills.Dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class DtoEvaluacionItem {
    private Integer idEvaluacionItem;
    
    @JsonProperty("evaluacion_id_evaluacion")
    private Integer evaluacion_idEvaluacion;
    
    @JsonProperty("item_id_item") 
    private Integer item_idItem;
    
    @JsonProperty("prueba_id_prueba")
    private Integer prueba_id_prueba;
    
    private Double valoracion;
}
