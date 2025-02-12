package com.andaluciaskills.andaluciasckills.Mapper;

import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Entity.Item;

public class ItemMapper implements GenericMapper<Item, DtoItem> {
    @Override
    public DtoItem toDto(Item entity) {
        if (entity == null) return null;
        
        DtoItem dto = new DtoItem();
        dto.setIdItem(entity.getIdItem());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPeso(entity.getPeso());
        dto.setGradosConsecucion(entity.getGradosConsecucion());
        return dto;
    }
    
    @Override
    public Item toEntity(DtoItem dto) {
        if (dto == null) return null;
        
        Item entity = new Item();
        entity.setIdItem(dto.getIdItem());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPeso(dto.getPeso());
        entity.setGradosConsecucion(dto.getGradosConsecucion());
        return entity;
    }
}

