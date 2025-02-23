package com.andaluciaskills.andaluciasckills.Dto;

import lombok.Data;

@Data
public class DtoItem {
    private Integer idItem;
    private String descripcion;
    private Integer peso;
    private Integer grados_consecucion;
    private Integer prueba_id_Prueba;
    private Double puntuacionMaxima;  // Añadido este campo

}

