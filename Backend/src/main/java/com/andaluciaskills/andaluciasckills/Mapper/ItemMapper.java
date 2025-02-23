package com.andaluciaskills.andaluciasckills.Mapper;

import org.springframework.stereotype.Component;

import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Entity.Item;

@Component 
public class ItemMapper implements GenericMapper<Item, DtoItem> {
    @Override
    public DtoItem toDto(Item entity) {
        if (entity == null) return null;
        
        DtoItem dto = new DtoItem();
        dto.setIdItem(entity.getIdItem());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPeso(entity.getPeso());
        dto.setGrados_consecucion(entity.getGradosConsecucion());
        dto.setPrueba_id_Prueba(entity.getPruebaIdPrueba());
        return dto;
    }
    
    @Override
    public Item toEntity(DtoItem dto) {
        if (dto == null) return null;
        
        Item entity = new Item();
        entity.setIdItem(dto.getIdItem());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPeso(dto.getPeso());
        entity.setGradosConsecucion(dto.getGrados_consecucion());
        entity.setPruebaIdPrueba(dto.getPrueba_id_Prueba());
        return entity;
    }
}

