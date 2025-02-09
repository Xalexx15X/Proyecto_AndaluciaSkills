package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;
    
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
    
public interface EvaluacionBaseService {
  Evaluacion save(Evaluacion evaluacion);
  Optional<Evaluacion> findById(Integer id);
  List<Evaluacion> findAll();
  Evaluacion update(Evaluacion evaluacion);
  void delete(Integer id);
}
