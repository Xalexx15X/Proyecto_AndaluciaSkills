package com.andaluciaskills.andaluciasckills.Mapper;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluancion;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;

public class EvaluacionMapper implements GenericMapper<Evaluacion, DtoEvaluancion> {

    @Override
    public DtoEvaluancion toDto(Evaluacion entity) {
        if (entity == null) return null;
        
        DtoEvaluancion dto = new DtoEvaluancion();
        dto.setIdEvaluacion(entity.getIdEvaluacion());
        dto.setNotaFinal(entity.getNotaFinal());
        return dto;
    }
    
    @Override
    public Evaluacion toEntity(DtoEvaluancion dto) {
        if (dto == null) return null;
        
        Evaluacion entity = new Evaluacion();
        entity.setIdEvaluacion(dto.getIdEvaluacion());
        entity.setNotaFinal(dto.getNotaFinal());
        return entity;
    }
}
