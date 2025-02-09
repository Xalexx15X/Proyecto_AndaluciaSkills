package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Entity.Participante;

public interface ParticipanteBaseService {
    Participante save(Participante participante);
    Optional<Participante> findById(Integer id);
    List<Participante> findAll();
    Participante update(Participante participante);
    void delete(Integer id);
}