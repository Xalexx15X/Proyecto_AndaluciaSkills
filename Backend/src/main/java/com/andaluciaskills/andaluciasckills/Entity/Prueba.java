package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name = "pruebas")
public class Prueba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prueba")
    private Integer idPrueba;

    @NotBlank(message = "El enunciado es obligatorio")
    @Column(name = "enunciado", nullable = false)
    private String enunciado;

    @Column(name = "puntuacion_maxima")
    private Integer puntuacionMaxima;

    @Column(name = "especialidad_id_especialidad")
    private Integer especialidad_idEspecialidad;
}