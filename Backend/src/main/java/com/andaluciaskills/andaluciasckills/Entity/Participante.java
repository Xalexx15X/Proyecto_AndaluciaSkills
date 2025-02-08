package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "participantes")
public class Participante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idParticipante;
    
    private String nombre;
    private String apellidos;
    private String centro;

    @ManyToOne
    @JoinColumn(name = "Especialidad_idEspecialidad")
    private Especialidad especialidad;
}