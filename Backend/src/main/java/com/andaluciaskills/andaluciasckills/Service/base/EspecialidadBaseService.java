package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;
import com.andaluciaskills.andaluciasckills.Dto.DtoEspecialidades;

public interface EspecialidadBaseService {
    DtoEspecialidades save(DtoEspecialidades dto);
    Optional<DtoEspecialidades> findById(Integer id);
    List<DtoEspecialidades> findAll();
    DtoEspecialidades update(DtoEspecialidades dto);
    void delete(Integer id);

}
