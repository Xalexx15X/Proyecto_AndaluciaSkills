package com.andaluciaskills.andaluciasckills.Mapper;

import com.andaluciaskills.andaluciasckills.Dto.DtoUser;
import com.andaluciaskills.andaluciasckills.Entity.User;

public class UserMapper implements GenericMapper<User, DtoUser> {
    
    @Override
    public DtoUser toDto (User entity) {
        if (entity == null) return null;
        
        DtoUser dto = new DtoUser();
        dto.setIdUser(entity.getIdUser());        
        dto.setRole(entity.getRole());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        dto.setNombre(entity.getNombre());
        dto.setApellidos(entity.getApellidos());
        dto.setDni(entity.getDni());
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
        return entity;
    }

}
