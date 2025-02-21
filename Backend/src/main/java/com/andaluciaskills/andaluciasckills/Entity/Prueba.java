package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "pruebas")
public class Prueba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prueba")
    private Integer idPrueba;

    @Column(name = "enunciado")
    private String enunciado;

    @Column(name = "puntuacion_maxima")
    private Integer puntuacionMaxima;

    @Column(name = "especialidad_id_especialidad")
    private Integer especialidad_idEspecialidad;
}