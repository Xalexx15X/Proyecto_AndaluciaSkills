package com.andaluciaskills.andaluciasckills.Mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.andaluciaskills.andaluciasckills.Dto.DtoEspecialidades;
import com.andaluciaskills.andaluciasckills.Entity.Especialidad;

@Component
public class EspecialidadMapper {
    
    public DtoEspecialidades toDto(Especialidad especialidad) {
        if (especialidad == null) return null;
        
        DtoEspecialidades dto = new DtoEspecialidades();
        dto.setIdEspecialidad(especialidad.getIdEspecialidad());
        dto.setNombre(especialidad.getNombre());
        dto.setCodigo(especialidad.getCodigo());
        return dto;
    }
    
    public Especialidad toEntity(DtoEspecialidades dto) {
        if (dto == null) return null;
        
        Especialidad especialidad = new Especialidad();
        especialidad.setIdEspecialidad(dto.getIdEspecialidad());
        especialidad.setNombre(dto.getNombre());
        especialidad.setCodigo(dto.getCodigo());
        return especialidad;
    }
    
    public List<DtoEspecialidades> toDtoList(List<Especialidad> especialidades) {
        return especialidades.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
