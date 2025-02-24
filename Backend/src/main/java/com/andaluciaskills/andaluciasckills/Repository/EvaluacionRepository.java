package com.andaluciaskills.andaluciasckills.Repository;

import com.andaluciaskills.andaluciasckills.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Integer> {
     
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Evaluacion e " +
           "WHERE e.prueba_idPrueba = :pruebaId AND e.participante_idParticipante = :participanteId")
    boolean existsByPruebaIdAndParticipanteId(@Param("pruebaId") Integer prueba_idPrueba, 
                                             @Param("participanteId") Integer participante_idParticipante);

    @Query("SELECT NEW map(" +
           "esp.nombre as especialidad, " +
           "CONCAT(p.nombre, ' ', p.apellidos) as nombreGanador, " +
           "AVG(e.notaFinal) as notaMedia) " +
           "FROM Evaluacion e " +
           "JOIN Prueba pr ON e.prueba_idPrueba = pr.idPrueba " +
           "JOIN Especialidad esp ON pr.especialidad_idEspecialidad = esp.idEspecialidad " +
           "JOIN Participante p ON e.participante_idParticipante = p.idParticipante " +
           "GROUP BY esp.nombre, p.nombre, p.apellidos " +
           "ORDER BY esp.nombre, AVG(e.notaFinal) DESC")
    List<Map<String, Object>> findGanadoresPorEspecialidad();
}