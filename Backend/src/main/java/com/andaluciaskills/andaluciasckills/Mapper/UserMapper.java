package com.andaluciaskills.andaluciasckills.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.andaluciaskills.andaluciasckills.Dto.DtoUser;
import com.andaluciaskills.andaluciasckills.Entity.Especialidad;
import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Repository.EspecialidadRepository;

@Component
public class UserMapper implements GenericMapper<User, DtoUser> {
    
    @Autowired
    private EspecialidadRepository especialidadRepository; 

    @Override
    public DtoUser toDto(User entity) {
        if (entity == null) return null;
        
        DtoUser dto = new DtoUser();
        dto.setIdUser(entity.getIdUser());
        dto.setRole(entity.getRole());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setNombre(entity.getNombre());
        dto.setApellidos(entity.getApellidos());
        dto.setDni(entity.getDni());
        
        if (entity.getEspecialidad() != null) {
            dto.setEspecialidad_idEspecialidad(entity.getEspecialidad().getIdEspecialidad());
        }
        
        return dto;
    }
    
    @Override
    public User toEntity(DtoUser dto) {
        if (dto == null) return null;
        
        User entity = new User();
        entity.setIdUser(dto.getIdUser());
        entity.setRole(dto.getRole());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setNombre(dto.getNombre());
        entity.setApellidos(dto.getApellidos());
        entity.setDni(dto.getDni());
        
        if (dto.getEspecialidad_idEspecialidad() != null) {
            Especialidad especialidad = especialidadRepository
                .findById(dto.getEspecialidad_idEspecialidad())
                .orElse(null);
            entity.setEspecialidad(especialidad);
        }
        
        return entity;
    }
}
