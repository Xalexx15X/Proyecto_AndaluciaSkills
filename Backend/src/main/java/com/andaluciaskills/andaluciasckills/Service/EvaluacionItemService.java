package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.EvaluacionItemBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluacionItemService implements EvaluacionItemBaseService {
    private final EvaluacionItemRepository evaluacionItemRepository;

    @Override
    public EvaluacionItem save(EvaluacionItem evaluacionItem) {
        return evaluacionItemRepository.save(evaluacionItem);
    }

    @Override
    public Optional<EvaluacionItem> findById(Integer id) {
        return evaluacionItemRepository.findById(id);
    }

    @Override
    public List<EvaluacionItem> findAll() {
        return evaluacionItemRepository.findAll();
    }

    @Override
    public EvaluacionItem update(EvaluacionItem evaluacionItem) {
        return evaluacionItemRepository.save(evaluacionItem);
    }

    @Override
    public void delete(Integer id) {
        evaluacionItemRepository.deleteById(id);
    }

}
