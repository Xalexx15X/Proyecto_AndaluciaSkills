package com.andaluciaskills.andaluciasckills.Mapper;

import com.andaluciaskills.andaluciasckills.Entity.Especialidad;
import com.andaluciaskills.andaluciasckills.Entity.Participante;
import com.andaluciaskills.andaluciasckills.Repository.EspecialidadRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.andaluciaskills.andaluciasckills.Dto.DtoParticipante;

@Component 
public class ParticipanteMapper implements GenericMapper<Participante, DtoParticipante> {
    @Autowired
    private EspecialidadRepository especialidadRepository; 
    
    @Override
    public DtoParticipante toDto (Participante entity) {
        if (entity == null) return null;
        
        DtoParticipante dto = new DtoParticipante();
        dto.setIdParticipante(entity.getIdParticipante());        
        dto.setNombre(entity.getNombre());
        dto.setApellidos(entity.getApellidos());
        dto.setCentro(entity.getCentro());

        if (entity.getEspecialidad() != null) {
            dto.setEspecialidad_idEspecialidad(entity.getEspecialidad().getIdEspecialidad());
        }

        return dto;
    }
    
    @Override
    public Participante toEntity(DtoParticipante dto) {
        if (dto == null) return null;
        
        Participante entity = new Participante();
        entity.setIdParticipante(dto.getIdParticipante());
        entity.setNombre(dto.getNombre());
        entity.setApellidos(dto.getApellidos());
        entity.setCentro(dto.getCentro());

        // Añade esto para manejar la especialidad
        if (dto.getEspecialidad_idEspecialidad() != null) {
            Especialidad especialidad = especialidadRepository
                .findById(dto.getEspecialidad_idEspecialidad())
                .orElse(null);
            entity.setEspecialidad(especialidad);
        }

        return entity;
    }
}
    