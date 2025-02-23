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

@Service
@RequiredArgsConstructor
public class ItemService implements ItemBaseService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public DtoItem save(DtoItem dto) {
        Item item = itemMapper.toEntity(dto);
        Item savedItem = itemRepository.save(item);
        return itemMapper.toDto(savedItem);
    }

    @Override
    public Optional<DtoItem> findById(Integer id) {
        return itemRepository.findById(id)
                .map(itemMapper::toDto);
    }

    @Override
    public List<DtoItem> findAll() {
        return itemMapper.toDtoList(itemRepository.findAll());
    }

    @Override
    public DtoItem update(DtoItem dto) {
        Item item = itemMapper.toEntity(dto);
        Item updatedItem = itemRepository.save(item);
        return itemMapper.toDto(updatedItem);
    }

    @Override
    public void delete(Integer id) {
        itemRepository.deleteById(id);
    }

    public List<DtoItem> saveAll(List<DtoItem> items) {
        try {
            // Convertir DTOs a entidades
            List<Item> entities = items.stream()
                .map(itemMapper::toEntity)
                .collect(Collectors.toList());
            
            // Guardar todas las entidades
            List<Item> savedItems = itemRepository.saveAll(entities);
            
            // Convertir las entidades guardadas de vuelta a DTOs
            return savedItems.stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            System.err.println("Error en ItemService.saveAll: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<DtoItem> findByPruebaId(Integer pruebaId) {
        return itemRepository.findByPruebaIdPrueba(pruebaId)
            .stream()
            .map(item -> {
                DtoItem dto = itemMapper.toDto(item);
                dto.setPuntuacionMaxima(10.0); // Establecemos un valor por defecto
                return dto;
            })
            .collect(Collectors.toList());
    }

    public List<DtoItem> findAllById(List<Integer> collect) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllById'");
    }
}