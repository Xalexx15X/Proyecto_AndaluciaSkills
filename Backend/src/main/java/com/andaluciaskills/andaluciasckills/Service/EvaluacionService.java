package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluancion;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
import com.andaluciaskills.andaluciasckills.Mapper.EvaluacionMapper;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionRepository;
import com.andaluciaskills.andaluciasckills.Service.base.EvaluacionBaseService;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluacionService implements EvaluacionBaseService {
    private final EvaluacionRepository evaluacionRepository;
    private final EvaluacionMapper evaluacionMapper;

    @Override
    public DtoEvaluancion save(DtoEvaluancion dto) {
        Evaluacion evaluacion = evaluacionMapper.toEntity(dto);
        Evaluacion savedEvaluacion = evaluacionRepository.save(evaluacion);
        return evaluacionMapper.toDto(savedEvaluacion);
    }

    @Override
    public Optional<DtoEvaluancion> findById(Integer id) {
        return evaluacionRepository.findById(id)
                .map(evaluacionMapper::toDto);
    }

    @Override
    public List<DtoEvaluancion> findAll() {
        return evaluacionMapper.toDtoList(evaluacionRepository.findAll());
    }

    @Override
    public DtoEvaluancion update(DtoEvaluancion dto) {
        Evaluacion evaluacion = evaluacionMapper.toEntity(dto);
        Evaluacion updatedEvaluacion = evaluacionRepository.save(evaluacion);
        return evaluacionMapper.toDto(updatedEvaluacion);
    }

    @Override
    public void delete(Integer id) {
        evaluacionRepository.deleteById(id);
    }

}