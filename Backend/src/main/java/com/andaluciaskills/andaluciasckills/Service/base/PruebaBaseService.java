package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;

public interface PruebaBaseService {
    DtoPrueba save(DtoPrueba prueba);
    Optional<DtoPrueba> findById(Integer id);
    List<DtoPrueba> findAll();
    DtoPrueba update(DtoPrueba prueba);
    void delete(Integer id);
}

