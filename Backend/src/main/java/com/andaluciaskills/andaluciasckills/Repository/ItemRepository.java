package com.andaluciaskills.andaluciasckills.Repository;

import com.andaluciaskills.andaluciasckills.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByPruebaIdPrueba(Integer pruebaIdPrueba);
}

