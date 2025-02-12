package com.andaluciaskills.andaluciasckills.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Entity.Item;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionRepository;
import com.andaluciaskills.andaluciasckills.Repository.ItemRepository;

@Component
public class EvaluacionItemMapper implements GenericMapper<EvaluacionItem, DtoEvaluacionItem> {

    @Autowired
    private EvaluacionRepository evaluacionRepository;
    private ItemRepository itemRepository;

    @Override
    public DtoEvaluacionItem toDto(EvaluacionItem entity) {
        if (entity == null) return null;
        
        DtoEvaluacionItem dto = new DtoEvaluacionItem();
        dto.setIdEvaluacionItem(entity.getIdEvaluacionItem());
        dto.setValoracion(entity.getValoracion());
        if (entity.getEvaluacion() != null) {
            dto.setEvaluacion_idEvaluacion(entity.getEvaluacion().getIdEvaluacion());
        }
        if (entity.getItem() != null) {
            dto.setItem_idItem(entity.getItem().getIdItem());
        }
        return dto;
    }

    @Override
    public EvaluacionItem toEntity(DtoEvaluacionItem dto) {
        if (dto == null) return null;
        
        EvaluacionItem entity = new EvaluacionItem();
        entity.setIdEvaluacionItem(dto.getIdEvaluacionItem());
        entity.setValoracion(dto.getValoracion());
        
        // para gestionar evaluaciones
        if (dto.getEvaluacion_idEvaluacion() != null) {
            Evaluacion evaluacion = evaluacionRepository
                .findById(dto.getEvaluacion_idEvaluacion())
                .orElse(null);
            entity.setEvaluacion(evaluacion);
        }

        // para gestionar items
        if (dto.getItem_idItem() != null) {
            Item item = itemRepository
                .findById(dto.getItem_idItem())
                .orElse(null);
            entity.setItem(item);
        }
        return entity;
    }
}
