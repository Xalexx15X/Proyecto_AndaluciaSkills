package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;
import com.andaluciaskills.andaluciasckills.Entity.Especialidad;

public interface EspecialidadBaseService {
    Especialidad save(Especialidad especialidad);
    Optional<Especialidad> findById(Integer id);  // Cambiado a Integer
    List<Especialidad> findAll();
    Especialidad update(Especialidad especialidad);
    void delete(Integer id);  // Cambiado a Integer
}
