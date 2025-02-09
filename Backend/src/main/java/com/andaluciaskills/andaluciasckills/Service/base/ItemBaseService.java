package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Entity.Item;

public interface ItemBaseService {
    Item save(Item item);
    Optional<Item> findById(Integer id);
    List<Item> findAll();
    Item update(Item item);
    void delete(Integer id);
}
