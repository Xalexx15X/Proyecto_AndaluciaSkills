package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idItem;
    
    private String descripcion;
    private Integer peso;
    private Integer gradosConsecucion;
}