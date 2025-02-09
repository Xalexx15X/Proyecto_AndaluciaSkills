package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
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

    @Override
    public Evaluacion save(Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    @Override
    public Optional<Evaluacion> findById(Integer id) {
        return evaluacionRepository.findById(id);
    }

    @Override
    public List<Evaluacion> findAll() {
        return evaluacionRepository.findAll();
    }

    @Override
    public Evaluacion update(Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }

    @Override
    public void delete(Integer id) {
        evaluacionRepository.deleteById(id);
    }

}