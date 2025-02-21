package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final EvaluacionService evaluacionService;

    @Override
    @Transactional
    public DtoEvaluacionItem save(DtoEvaluacionItem dto) {
        try {
            System.out.println("Guardando EvaluacionItem: " + dto);
            
            // Verificar que los IDs necesarios no sean null
            if (dto.getEvaluacion_idEvaluacion() == null) {
                throw new IllegalArgumentException("El ID de evaluación no puede ser null");
            }
            if (dto.getItem_idItem() == null) {
                throw new IllegalArgumentException("El ID del item no puede ser null");
            }
            if (dto.getPrueba_idPrueba() == null) {
                throw new IllegalArgumentException("El ID de la prueba no puede ser null");
            }

            EvaluacionItem evaluacionItem = evaluacionItemMapper.toEntity(dto);
            EvaluacionItem savedEvaluacionItem = evaluacionItemRepository.save(evaluacionItem);
            System.out.println("EvaluacionItem guardado con ID: " + savedEvaluacionItem.getIdEvaluacionItem());
            
            // Actualizar nota final de la evaluación si es necesario
            if (dto.getValoracion() != null) {
                evaluacionService.actualizarNotaFinal(dto.getEvaluacion_idEvaluacion());
            }
            
            return evaluacionItemMapper.toDto(savedEvaluacionItem);
        } catch (Exception e) {
            System.err.println("Error al guardar EvaluacionItem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DtoEvaluacionItem> findById(Integer id) {
        return evaluacionItemRepository.findById(id)
                .map(evaluacionItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoEvaluacionItem> findAll() {
        return evaluacionItemMapper.toDtoList(evaluacionItemRepository.findAll());
    }

    @Override
    @Transactional
    public DtoEvaluacionItem update(DtoEvaluacionItem dto) {
        if (dto.getIdEvaluacionItem() == null) {
            throw new IllegalArgumentException("No se puede actualizar un EvaluacionItem sin ID");
        }
        
        EvaluacionItem evaluacionItem = evaluacionItemMapper.toEntity(dto);
        EvaluacionItem updatedEvaluacionItem = evaluacionItemRepository.save(evaluacionItem);
        
        // Actualizar nota final de la evaluación
        evaluacionService.actualizarNotaFinal(dto.getEvaluacion_idEvaluacion());
        
        return evaluacionItemMapper.toDto(updatedEvaluacionItem);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        evaluacionItemRepository.deleteById(id);
    }

    // Método auxiliar para buscar por evaluación ID
    @Transactional(readOnly = true)
    public List<DtoEvaluacionItem> findByEvaluacionId(Integer evaluacionId) {
        List<EvaluacionItem> items = evaluacionItemRepository.findByEvaluacionIdEvaluacion(evaluacionId);
        return evaluacionItemMapper.toDtoList(items);
    }
}
