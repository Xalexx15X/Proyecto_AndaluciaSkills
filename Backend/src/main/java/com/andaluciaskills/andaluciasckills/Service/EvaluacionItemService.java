package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Mapper.EvaluacionItemMapper;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.EvaluacionItemBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluacionItemService implements EvaluacionItemBaseService {
    private final EvaluacionItemRepository evaluacionItemRepository;
    private final EvaluacionItemMapper evaluacionItemMapper;

    @Override
    public DtoEvaluacionItem save(DtoEvaluacionItem dto) {
        EvaluacionItem evaluacionItem = evaluacionItemMapper.toEntity(dto);
        EvaluacionItem savedEvaluacionItem = evaluacionItemRepository.save(evaluacionItem);
        return evaluacionItemMapper.toDto(savedEvaluacionItem);
    }

    @Override
    public Optional<DtoEvaluacionItem> findById(Integer id) {
        return evaluacionItemRepository.findById(id)
                .map(evaluacionItemMapper::toDto);
    }

    @Override
    public List<DtoEvaluacionItem> findAll() {
        return evaluacionItemMapper.toDtoList(evaluacionItemRepository.findAll());
    }

    @Override
    public DtoEvaluacionItem update(DtoEvaluacionItem dto) {
        EvaluacionItem evaluacion = evaluacionItemMapper.toEntity(dto);
        EvaluacionItem updatedEvaluacionItem = evaluacionItemRepository.save(evaluacion);
        return evaluacionItemMapper.toDto(updatedEvaluacionItem);
    }

    @Override
    public void delete(Integer id) {
        evaluacionItemRepository.deleteById(id);
    }

}
