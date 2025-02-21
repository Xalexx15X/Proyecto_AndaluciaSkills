package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacion;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Mapper.EvaluacionMapper;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionRepository;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.EvaluacionBaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluacionService implements EvaluacionBaseService {
    private final EvaluacionRepository evaluacionRepository;
    private final EvaluacionMapper evaluacionMapper;
    private final EvaluacionItemRepository evaluacionItemRepository;

    @Transactional(readOnly = true)
    public Double calcularNotaFinal(Integer evaluacionId) {
        List<EvaluacionItem> items = evaluacionItemRepository.findByEvaluacionIdEvaluacion(evaluacionId);
        if (items.isEmpty()) {
            return 0.0;
        }
        
        double sumaPonderada = 0.0;
        double sumaPesos = 0.0;
        
        for (EvaluacionItem item : items) {
            if (item.getValoracion() != null) {
                sumaPonderada += item.getValoracion();
                sumaPesos += 1; // Incrementar el contador de items valorados
            }
        }
        
        return sumaPesos > 0 ? sumaPonderada / sumaPesos : 0.0;
    }

    @Transactional
    public void actualizarNotaFinal(Integer evaluacionId) {
        Double notaFinal = calcularNotaFinal(evaluacionId);
        evaluacionRepository.findById(evaluacionId).ifPresent(evaluacion -> {
            evaluacion.setNotaFinal(notaFinal);
            evaluacionRepository.save(evaluacion);
        });
    }

    @Override
    @Transactional
    public DtoEvaluacion save(DtoEvaluacion dto) {
        try {
            Evaluacion evaluacion = evaluacionMapper.toEntity(dto);
            Evaluacion savedEvaluacion = evaluacionRepository.save(evaluacion);
            
            // Calcular y actualizar nota final
            Double notaFinal = calcularNotaFinal(savedEvaluacion.getIdEvaluacion());
            savedEvaluacion.setNotaFinal(notaFinal);
            savedEvaluacion = evaluacionRepository.save(savedEvaluacion);
            
            return evaluacionMapper.toDto(savedEvaluacion);
        } catch (Exception e) {
            System.err.println("Error al guardar evaluación: " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DtoEvaluacion> findById(Integer id) {
        return evaluacionRepository.findById(id)
                .map(evaluacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DtoEvaluacion> findAll() {
        return evaluacionMapper.toDtoList(evaluacionRepository.findAll());
    }

    @Override
    @Transactional
    public DtoEvaluacion update(DtoEvaluacion dto) {
        if (dto.getIdEvaluacion() == null) {
            throw new IllegalArgumentException("No se puede actualizar una evaluación sin ID");
        }
        
        Evaluacion evaluacion = evaluacionMapper.toEntity(dto);
        Evaluacion updatedEvaluacion = evaluacionRepository.save(evaluacion);
        
        // Recalcular nota final después de la actualización
        actualizarNotaFinal(updatedEvaluacion.getIdEvaluacion());
        
        return evaluacionMapper.toDto(updatedEvaluacion);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        evaluacionRepository.deleteById(id);
    }
}