package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Entity.Especialidad;
import com.andaluciaskills.andaluciasckills.Repository.EspecialidadRepository;
import com.andaluciaskills.andaluciasckills.Service.base.EspecialidadBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EspecialidadService implements EspecialidadBaseService {
    private final EspecialidadRepository especialidadRepository;

    @Override
    public Especialidad save(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    @Override
    public Optional<Especialidad> findById(Integer id) {  
        return especialidadRepository.findById(id);
    }

    @Override
    public List<Especialidad> findAll() {
        return especialidadRepository.findAll();
    }

    @Override
    public Especialidad update(Especialidad especialidad) {
        return especialidadRepository.save(especialidad);
    }

    @Override
    public void delete(Integer id) {  // Cambiado a Integer
        especialidadRepository.deleteById(id);
    }
}

