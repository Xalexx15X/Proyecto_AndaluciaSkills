package com.andaluciaskills.andaluciasckills.Mapper;

import com.andaluciaskills.andaluciasckills.Dto.DtoEspecialidades;
import com.andaluciaskills.andaluciasckills.Entity.Especialidad;

public class EspecialidadMapper implements GenericMapper<Especialidad, DtoEspecialidades> {
    @Override
    public DtoEspecialidades toDto(Especialidad entity) {
        if (entity == null) return null;
        
        DtoEspecialidades dto = new DtoEspecialidades();
        dto.setIdEspecialidad(entity.getIdEspecialidad());
        dto.setNombre(entity.getNombre());
        dto.setCodigo(entity.getCodigo());
        return dto;
    }
    
    @Override
    public Especialidad toEntity(DtoEspecialidades dto) {
        if (dto == null) return null;
        
        Especialidad entity = new Especialidad();
        entity.setIdEspecialidad(dto.getIdEspecialidad());
        entity.setNombre(dto.getNombre());
        entity.setCodigo(dto.getCodigo());
        return entity;
    }
}
