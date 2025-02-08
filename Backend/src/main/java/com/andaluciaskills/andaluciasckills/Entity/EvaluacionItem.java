package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "evaluacion_items")
public class EvaluacionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluacionItem;
    
    private Integer valoracion;

    @ManyToOne
    @JoinColumn(name = "Evaluacion_idEvaluacion")
    private Evaluacion evaluacion;

    @ManyToOne
    @JoinColumn(name = "Item_idItem")
    private Item item;
}