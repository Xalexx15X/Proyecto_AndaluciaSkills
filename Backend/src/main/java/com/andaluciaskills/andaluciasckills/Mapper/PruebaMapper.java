package com.andaluciaskills.andaluciasckills.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;
import com.andaluciaskills.andaluciasckills.Entity.Especialidad;
import com.andaluciaskills.andaluciasckills.Entity.Prueba;
import com.andaluciaskills.andaluciasckills.Repository.EspecialidadRepository;

@Component 
public class PruebaMapper implements GenericMapper<Prueba, DtoPrueba> {

@Autowired
private EspecialidadRepository especialidadRepository;

@Override
    public DtoPrueba toDto (Prueba entity) {
        if (entity == null) return null;
        
        DtoPrueba dto = new DtoPrueba();
        dto.setIdPrueba(entity.getIdPrueba());        
        dto.setEnunciado(entity.getEnunciado());
        dto.setPuntuacionMaxima(entity.getPuntuacionMaxima());

        if (entity.getEspecialidad() != null) {
            dto.setEspecialidad_idEspecialidad(entity.getEspecialidad().getIdEspecialidad());
        }
        
        return dto;
    }
    
    @Override
    public Prueba toEntity(DtoPrueba dto) {
        if (dto == null) return null;
        
        Prueba entity = new Prueba();
        entity.setIdPrueba(dto.getIdPrueba());
        entity.setEnunciado(dto.getEnunciado());
        entity.setPuntuacionMaxima(dto.getPuntuacionMaxima());

        if (dto.getEspecialidad_idEspecialidad() != null) {
            Especialidad especialidad = especialidadRepository
                .findById(dto.getEspecialidad_idEspecialidad())
                .orElse(null);
            entity.setEspecialidad(especialidad);
        }
        return entity;
    }
}
