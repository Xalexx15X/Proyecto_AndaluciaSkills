package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "evaluaciones")
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluacion;
    
    private Double notaFinal;

    @ManyToOne
    @JoinColumn(name = "Participante_idParticipante")
    private Participante participante;

    @ManyToOne
    @JoinColumn(name = "User_idUser")
    private User user;

    @ManyToOne
    @JoinColumn(name = "prueba_idPrueba")
    private Prueba prueba;
}
