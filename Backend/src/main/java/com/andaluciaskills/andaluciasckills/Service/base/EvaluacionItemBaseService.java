package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;


public interface EvaluacionItemBaseService {
    DtoEvaluacionItem save(DtoEvaluacionItem dto);
    Optional<DtoEvaluacionItem> findById(Integer id);
    List<DtoEvaluacionItem> findAll();
    DtoEvaluacionItem update(DtoEvaluacionItem dto);
    void delete(Integer id);
}
