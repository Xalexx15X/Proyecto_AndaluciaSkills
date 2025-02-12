package com.andaluciaskills.andaluciasckills.Mapper;

import com.andaluciaskills.andaluciasckills.Entity.Participante;
import com.andaluciaskills.andaluciasckills.Dto.DtoParticipante;

public class ParticipanteMapper implements GenericMapper<Participante, DtoParticipante> {

@Override
    public DtoParticipante toDto (Participante entity) {
        if (entity == null) return null;
        
        DtoParticipante dto = new DtoParticipante();
        dto.setIdParticipante(entity.getIdParticipante());        
        dto.setNombre(entity.getNombre());
        dto.setApellidos(entity.getApellidos());
        dto.setCentro(entity.getCentro());
        return dto;
    }
    
    @Override
    public Participante toEntity(DtoParticipante dto) {
        if (dto == null) return null;
        
        Participante entity = new Participante();
        entity.setIdParticipante(dto.getIdParticipante());
        entity.setNombre(dto.getNombre());
        entity.setApellidos(dto.getApellidos());
        entity.setCentro(dto.getCentro());
        return entity;
    }
}
    