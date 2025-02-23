package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "evaluacion_items")
public class EvaluacionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion_item")
    private Integer idEvaluacionItem;

    @Column(name = "evaluacion_id_evaluacion")
    private Integer evaluacion_idEvaluacion;

    @Column(name = "item_id_item")
    private Integer item_idItem;

    @Column(name = "prueba_id_prueba")
    private Integer prueba_idPrueba;

    @Column(name = "valoracion")
    private Double valoracion;

    
}