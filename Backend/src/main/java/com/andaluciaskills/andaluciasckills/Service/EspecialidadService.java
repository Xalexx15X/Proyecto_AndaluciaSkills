package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoEspecialidades;
import com.andaluciaskills.andaluciasckills.Entity.Especialidad;
import com.andaluciaskills.andaluciasckills.Mapper.EspecialidadMapper;
import com.andaluciaskills.andaluciasckills.Repository.EspecialidadRepository;
import com.andaluciaskills.andaluciasckills.Service.base.EspecialidadBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EspecialidadService implements EspecialidadBaseService {
    private final EspecialidadRepository especialidadRepository;
    private final EspecialidadMapper especialidadMapper;

    @Override
    public DtoEspecialidades save(DtoEspecialidades dto) {
        Especialidad especialidad = especialidadMapper.toEntity(dto);
        Especialidad savedEspecialidad = especialidadRepository.save(especialidad);
        return especialidadMapper.toDto(savedEspecialidad);
    }

    @Override
    public Optional<DtoEspecialidades> findById(Integer id) {
        return especialidadRepository.findById(id)
                .map(especialidadMapper::toDto);
    }

    @Override
    public List<DtoEspecialidades> findAll() {
        return especialidadMapper.toDtoList(especialidadRepository.findAll());
    }

    @Override
    public DtoEspecialidades update(DtoEspecialidades dto) {
        Especialidad especialidad = especialidadMapper.toEntity(dto);
        Especialidad updatedEspecialidad = especialidadRepository.save(especialidad);
        return especialidadMapper.toDto(updatedEspecialidad);
    }

    @Override
    public void delete(Integer id) {
        especialidadRepository.deleteById(id);
    }
}

