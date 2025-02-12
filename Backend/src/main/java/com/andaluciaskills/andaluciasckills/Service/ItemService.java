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
}