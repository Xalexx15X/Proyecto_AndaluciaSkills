package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Entity.Prueba;

public interface PruebaBaseService {
    Prueba save(Prueba prueba);
    Optional<Prueba> findById(Integer id);
    List<Prueba> findAll();
    Prueba update(Prueba prueba);
    void delete(Integer id);
}

