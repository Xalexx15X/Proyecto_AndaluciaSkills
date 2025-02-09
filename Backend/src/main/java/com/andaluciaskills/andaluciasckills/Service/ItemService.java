package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Entity.Item;
import com.andaluciaskills.andaluciasckills.Repository.ItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.ItemBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService implements ItemBaseService {
    private final ItemRepository itemRepository;

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Optional<Item> findById(Integer id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item update(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void delete(Integer id) {
        itemRepository.deleteById(id);
    }
}