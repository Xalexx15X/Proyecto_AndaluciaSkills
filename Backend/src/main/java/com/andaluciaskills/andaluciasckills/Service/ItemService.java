package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Entity.Item;
import com.andaluciaskills.andaluciasckills.Mapper.ItemMapper;
import com.andaluciaskills.andaluciasckills.Repository.ItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.ItemBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // Indica que esta clase es un servicio de Spring
@RequiredArgsConstructor // Genera constructor con campos final automáticamente
public class ItemService implements ItemBaseService {
    // Inyección de dependencias
    private final ItemRepository itemRepository; // Para operaciones CRUD con la BD
    private final ItemMapper itemMapper; // Para convertir entre Entity y DTO

    // Método para guardar un nuevo item
    @Override
    public DtoItem save(DtoItem dto) {
        // 1. Convierte el DTO a entidad
        Item item = itemMapper.toEntity(dto);
        // 2. Guarda la entidad en la base de datos
        Item savedItem = itemRepository.save(item);
        // 3. Convierte la entidad guardada de vuelta a DTO
        return itemMapper.toDto(savedItem);
    }

    // Método para buscar un item por su ID
    @Override
    public Optional<DtoItem> findById(Integer id) {
        // Busca el item y si lo encuentra lo convierte a DTO
        return itemRepository.findById(id)
                .map(itemMapper::toDto);
    }

    // Método para obtener todos los items
    @Override
    public List<DtoItem> findAll() {
        // Obtiene todos los items y los convierte a DTOs
        return itemMapper.toDtoList(itemRepository.findAll());
    }

    // Método para actualizar un item existente
    @Override
    public DtoItem update(DtoItem dto) {
        // 1. Convierte el DTO a entidad
        Item item = itemMapper.toEntity(dto);
        // 2. Guarda los cambios en la base de datos
        Item updatedItem = itemRepository.save(item);
        // 3. Convierte la entidad actualizada a DTO
        return itemMapper.toDto(updatedItem);
    }

    // Método para eliminar un item por su ID
    @Override
    public void delete(Integer id) {
        itemRepository.deleteById(id);
    }

    // Método para guardar múltiples items a la vez
    public List<DtoItem> saveAll(List<DtoItem> items) {
        try {
            // 1. Convierte la lista de DTOs a entidades usando Stream
            List<Item> entities = items.stream()
                .map(itemMapper::toEntity)
                .collect(Collectors.toList());
            
            // 2. Guarda todas las entidades en la base de datos
            List<Item> savedItems = itemRepository.saveAll(entities);
            
            // 3. Convierte las entidades guardadas de vuelta a DTOs
            return savedItems.stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            // Manejo de errores con logging
            System.err.println("Error en ItemService.saveAll: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Método para encontrar items por ID de prueba
    public List<DtoItem> findByPruebaId(Integer pruebaId) {
        return itemRepository.findByPruebaIdPrueba(pruebaId)
            .stream()
            .map(item -> {
                DtoItem dto = itemMapper.toDto(item);
                dto.setPuntuacionMaxima(10.0); // Valor por defecto para puntuación máxima
                return dto;
            })
            .collect(Collectors.toList());
    }

    // Método pendiente de implementar para buscar items por lista de IDs
    public List<DtoItem> findAllById(List<Integer> collect) {
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }
}