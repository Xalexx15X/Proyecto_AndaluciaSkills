package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Dto.DtoParticipante;

public interface ParticipanteBaseService {
    DtoParticipante save(DtoParticipante participante);
    Optional<DtoParticipante> findById(Integer id);
    List<DtoParticipante> findAll();
    DtoParticipante update(DtoParticipante participante);
    void delete(Integer id);
}