package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacion;
    
public interface EvaluacionBaseService {
    DtoEvaluacion save(DtoEvaluacion dto);
    Optional<DtoEvaluacion> findById(Integer id);
    List<DtoEvaluacion> findAll();
    DtoEvaluacion update(DtoEvaluacion dto);
    void delete(Integer id);
}
