package com.andaluciaskills.andaluciasckills.Mapper;

import org.springframework.stereotype.Component;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;


@Component
public class EvaluacionItemMapper implements GenericMapper<EvaluacionItem, DtoEvaluacionItem> {

   

    @Override
    public DtoEvaluacionItem toDto(EvaluacionItem entity) {
        if (entity == null) return null;
        
        DtoEvaluacionItem dto = new DtoEvaluacionItem();
        dto.setIdEvaluacionItem(entity.getIdEvaluacionItem());
        dto.setValoracion(entity.getValoracion().doubleValue());
        dto.setEvaluacion_idEvaluacion(entity.getEvaluacion_idEvaluacion());
        dto.setItem_idItem(entity.getItem_idItem());
        return dto;
    }

    @Override
    public EvaluacionItem toEntity(DtoEvaluacionItem dto) {
        if (dto == null) return null;
        
        EvaluacionItem entity = new EvaluacionItem();
        entity.setIdEvaluacionItem(dto.getIdEvaluacionItem());
        entity.setValoracion(dto.getValoracion());
        entity.setEvaluacion_idEvaluacion(dto.getEvaluacion_idEvaluacion());
        entity.setItem_idItem(dto.getItem_idItem());
        return entity;
    }
}
