package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluancion;
    
public interface EvaluacionBaseService {
    DtoEvaluancion save(DtoEvaluancion dto);
    Optional<DtoEvaluancion> findById(Integer id);
    List<DtoEvaluancion> findAll();
    DtoEvaluancion update(DtoEvaluancion dto);
    void delete(Integer id);
}
