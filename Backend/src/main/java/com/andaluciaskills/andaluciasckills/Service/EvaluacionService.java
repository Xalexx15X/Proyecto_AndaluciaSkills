package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacion;
import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Mapper.EvaluacionMapper;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionRepository;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.EvaluacionBaseService;
import com.andaluciaskills.andaluciasckills.Service.ItemService;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionService implements EvaluacionBaseService {
    private final EvaluacionRepository evaluacionRepository;
    private final EvaluacionMapper evaluacionMapper;
    private final EvaluacionItemRepository evaluacionItemRepository;
    private final ItemService itemService; // Añadir esta dependencia

    public EvaluacionService(EvaluacionRepository evaluacionRepository, 
                           EvaluacionMapper evaluacionMapper,
                           EvaluacionItemRepository evaluacionItemRepository,
                           ItemService itemService) {
        this.evaluacionRepository = evaluacionRepository;
        this.evaluacionMapper = evaluacionMapper;
        this.evaluacionItemRepository = evaluacionItemRepository;
        this.itemService = itemService;
    }

    public Double calcularNotaFinal(Integer evaluacionId) {
        List<EvaluacionItem> items = evaluacionItemRepository.findByEvaluacionIdEvaluacion(evaluacionId);
        if (items.isEmpty()) {
            return 0.0;
        }
        
        double sumaPonderada = 0.0;
        double sumaPesos = 0.0;
        
        for (EvaluacionItem evaluacionItem : items) {
            if (evaluacionItem.getValoracion() != null) {
                // Obtener el Item asociado y su peso
                Optional<DtoItem> item = itemService.findById(evaluacionItem.getItem_idItem());
                double peso = item.map(i -> i.getPeso() != null ? i.getPeso() : 1.0).orElse(1.0);
                
                sumaPonderada += (evaluacionItem.getValoracion() * peso);
                sumaPesos += peso;
                
                System.out.println("Item ID: " + evaluacionItem.getItem_idItem() + 
                                 ", Valoración: " + evaluacionItem.getValoracion() +
                                 ", Peso: " + peso);
            }
        }
        
        Double notaFinal = sumaPesos > 0 ? sumaPonderada / sumaPesos : 0.0;
        notaFinal = Math.round(notaFinal * 100.0) / 100.0;
        
        System.out.println("Nota final calculada para evaluación " + evaluacionId + ": " + notaFinal);
        return notaFinal;
    }

    public void actualizarNotaFinal(Integer evaluacionId) {
        Double notaFinal = calcularNotaFinal(evaluacionId);
        evaluacionRepository.findById(evaluacionId).ifPresent(evaluacion -> {
            evaluacion.setNotaFinal(notaFinal);
            evaluacionRepository.save(evaluacion);
        });
    }

    @Override
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
    public Optional<DtoEvaluacion> findById(Integer id) {
        return evaluacionRepository.findById(id)
                .map(evaluacionMapper::toDto);
    }

    @Override
    public List<DtoEvaluacion> findAll() {
        return evaluacionMapper.toDtoList(evaluacionRepository.findAll());
    }

    @Override
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
    public void delete(Integer id) {
        evaluacionRepository.deleteById(id);
    }

    public DtoEvaluacion crearEvaluacion(Integer pruebaId, Integer participanteId, Integer userId, Double notaFinal) {
        try {
            System.out.println("Iniciando transacción para crear evaluación");
            
            Evaluacion evaluacion = new Evaluacion();
            evaluacion.setPrueba_idPrueba(pruebaId);
            evaluacion.setParticipante_idParticipante(participanteId);
            evaluacion.setUser_idUser(userId);
            evaluacion.setNotaFinal(notaFinal);

            System.out.println("Guardando evaluación en la base de datos...");
            
            Evaluacion evaluacionGuardada = evaluacionRepository.save(evaluacion); // Cambiado save por saveAndFlush

            if (evaluacionGuardada.getIdEvaluacion() == null) {
                throw new RuntimeException("No se pudo obtener el ID de la evaluación guardada");
            }

            Optional<Evaluacion> verificacion = evaluacionRepository.findById(evaluacionGuardada.getIdEvaluacion());
            if (!verificacion.isPresent()) {
                throw new RuntimeException("La evaluación no se encontró después de guardarla");
            }

            DtoEvaluacion dto = evaluacionMapper.toDto(evaluacionGuardada);
            
            return dto;
        } catch (Exception e) {
            System.err.println("Error en crearEvaluacion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear la evaluación", e);
        }
    }

    public DtoEvaluacion actualizarNotaFinal(Integer evaluacionId, Double notaFinal) {
        try {
            System.out.println("Actualizando nota final para evaluación " + evaluacionId + ": " + notaFinal);
            
            Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con ID: " + evaluacionId));
            
            evaluacion.setNotaFinal(notaFinal);
            Evaluacion evaluacionActualizada = evaluacionRepository.save(evaluacion);
            
            System.out.println("Nota final actualizada correctamente");
            return evaluacionMapper.toDto(evaluacionActualizada);
        } catch (Exception e) {
            System.err.println("Error al actualizar nota final: " + e.getMessage());
            throw new RuntimeException("Error al actualizar la nota final", e);
        }
    }

    public DtoEvaluacion actualizarNotaFinalAutomatica(Integer evaluacionId) {
        try {
            System.out.println("Calculando y actualizando nota final para evaluación: " + evaluacionId);
            
            // Calcular la nota final
            Double notaFinal = calcularNotaFinal(evaluacionId);
            
            // Actualizar la evaluación con la nota calculada
            Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con ID: " + evaluacionId));
            
            evaluacion.setNotaFinal(notaFinal);
            Evaluacion evaluacionActualizada = evaluacionRepository.save(evaluacion);
            
            System.out.println("Nota final actualizada automáticamente: " + notaFinal);
            return evaluacionMapper.toDto(evaluacionActualizada);
        } catch (Exception e) {
            System.err.println("Error al actualizar nota final automática: " + e.getMessage());
            throw new RuntimeException("Error al actualizar la nota final automáticamente", e);
        }
    }
}