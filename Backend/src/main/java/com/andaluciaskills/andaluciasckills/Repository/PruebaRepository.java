package com.andaluciaskills.andaluciasckills.Repository;

import com.andaluciaskills.andaluciasckills.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PruebaRepository extends JpaRepository<Prueba, Integer> {
    @Query("SELECT p FROM Prueba p WHERE p.especialidad_idEspecialidad = :especialidadId")
    List<Prueba> findByEspecialidadId(@Param("especialidadId") Integer especialidadId);
}

