package com.andaluciaskills.andaluciasckills.Repository;

import com.andaluciaskills.andaluciasckills.Entity.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipanteRepository extends JpaRepository<Participante, Integer> {
    List<Participante> findByEspecialidadIdEspecialidad(Integer especialidadId);
}

