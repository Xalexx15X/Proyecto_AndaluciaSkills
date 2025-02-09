package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;

public interface EvaluacionItemBaseService {
    EvaluacionItem save(EvaluacionItem evaluacionItem);
    Optional<EvaluacionItem> findById(Integer id);
    List<EvaluacionItem> findAll();
    EvaluacionItem update(EvaluacionItem evaluacionItem);
    void delete(Integer id); 
}
