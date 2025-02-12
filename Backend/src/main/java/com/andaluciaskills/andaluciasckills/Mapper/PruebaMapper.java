package com.andaluciaskills.andaluciasckills.Mapper;

import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;
import com.andaluciaskills.andaluciasckills.Entity.Prueba;

public class PruebaMapper implements GenericMapper<Prueba, DtoPrueba> {
@Override
    public DtoPrueba toDto (Prueba entity) {
        if (entity == null) return null;
        
        DtoPrueba dto = new DtoPrueba();
        dto.setIdPrueba(entity.getIdPrueba());        
        dto.setEnunciado(entity.getEnunciado());
        dto.setPuntuacionMaxima(entity.getPuntuacionMaxima());
        return dto;
    }
    
    @Override
    public Prueba toEntity(DtoPrueba dto) {
        if (dto == null) return null;
        
        Prueba entity = new Prueba();
        entity.setIdPrueba(dto.getIdPrueba());
        entity.setEnunciado(dto.getEnunciado());
        entity.setPuntuacionMaxima(dto.getPuntuacionMaxima());
        return entity;
    }
}
