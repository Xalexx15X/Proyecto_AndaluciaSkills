package com.andaluciaskills.andaluciasckills.Repository;

import com.andaluciaskills.andaluciasckills.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Integer> {
     
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Evaluacion e " +
           "WHERE e.prueba_idPrueba = :pruebaId AND e.participante_idParticipante = :participanteId")
    boolean existsByPruebaIdAndParticipanteId(@Param("pruebaId") Integer prueba_idPrueba, 
                                             @Param("participanteId") Integer participante_idParticipante);
}