package com.andaluciaskills.andaluciasckills.Mapper;

import java.util.List;
import java.util.stream.Collectors;

public interface GenericMapper<E, D> {
    
    /**
     * Convierte una entidad a DTO
     * @param entity la entidad a convertir
     * @return el DTO resultante
     */
    D toDto(E entity);

    /**
     * Convierte un DTO a entidad
     * @param dto el DTO a convertir
     * @return la entidad resultante
     */
    E toEntity(D dto);

    /**
     * Convierte una lista de entidades a lista de DTOs
     * @param entities lista de entidades
     * @return lista de DTOs
     */
    default List<D> toDtoList(List<E> entities) {
        if (entities == null) return null;
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una lista de DTOs a lista de entidades
     * @param dtos lista de DTOs
     * @return lista de entidades
     */
    default List<E> toEntityList(List<D> dtos) {
        if (dtos == null) return null;
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
