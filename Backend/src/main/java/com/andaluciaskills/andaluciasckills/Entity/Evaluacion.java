package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "evaluaciones")
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Esto hace que sea autonumérico
    @Column(name = "id_evaluacion")
    private Integer idEvaluacion;
    
    @Column(name = "nota_final")
    private Double notaFinal;
    
    @Column(name = "participante_id_participante")
    private Integer participante_idParticipante;
    
    @Column(name = "prueba_id_prueba")
    private Integer prueba_idPrueba;
    
    @Column(name = "user_id_user")
    private Integer user_idUser;
}
