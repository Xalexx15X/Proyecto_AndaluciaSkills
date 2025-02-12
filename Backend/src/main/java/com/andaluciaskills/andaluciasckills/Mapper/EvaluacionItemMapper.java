package com.andaluciaskills.andaluciasckills.Mapper;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;

public class EvaluacionItemMapper implements GenericMapper<EvaluacionItem, DtoEvaluacionItem> {

    @Override
    public DtoEvaluacionItem toDto(EvaluacionItem entity) {
        if (entity == null) return null;
        
        DtoEvaluacionItem dto = new DtoEvaluacionItem();
        dto.setIdEvaluacionItem(entity.getIdEvaluacionItem());
        dto.setValoracion(entity.getValoracion());
        return dto;
    }
    
    @Override
    public EvaluacionItem toEntity(DtoEvaluacionItem dto) {
        if (dto == null) return null;
        
        EvaluacionItem entity = new EvaluacionItem();
        entity.setIdEvaluacionItem(dto.getIdEvaluacionItem());
        entity.setValoracion(dto.getValoracion());
        return entity;
    }
}
