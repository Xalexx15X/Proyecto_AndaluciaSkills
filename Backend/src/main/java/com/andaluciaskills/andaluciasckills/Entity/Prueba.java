package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "pruebas")
public class Prueba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPrueba;
    
    private String enunciado;
    private Integer puntuacionMaxima;

    @ManyToOne
    @JoinColumn(name = "Especialidad_idEspecialidad")
    private Especialidad especialidad;
}