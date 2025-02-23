package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "evaluacion")
@Data
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvaluacion;

    @Column(name = "prueba_idPrueba")
    private Integer prueba_idPrueba;

    @Column(name = "participante_idParticipante")
    private Integer participante_idParticipante;

    @Column(name = "notaFinal")
    private Double notaFinal;

    @Column(name = "user_idUser")
    private Integer user_idUser;
}
