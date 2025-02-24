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
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EvaluacionItemService implements EvaluacionItemBaseService {
    private final EvaluacionItemRepository evaluacionItemRepository;
    private final EvaluacionItemMapper evaluacionItemMapper;
    private final EvaluacionService evaluacionService;

    @Override
    public DtoEvaluacionItem save(DtoEvaluacionItem dto) {
        try {
            System.out.println("Guardando EvaluacionItem: " + dto);
            
            // Validar campos
            if (dto.getEvaluacion_idEvaluacion() == null) {
                throw new IllegalArgumentException("El ID de evaluación no puede ser null");
            }
            if (dto.getItem_idItem() == null) {
                throw new IllegalArgumentException("El ID del item no puede ser null");
            }
            if (dto.getPrueba_id_prueba() == null) {
                throw new IllegalArgumentException("El ID de la prueba no puede ser null");
            }

            // Log adicional para verificar los valores
            System.out.println("ID Evaluación: " + dto.getEvaluacion_idEvaluacion());
            System.out.println("ID Item: " + dto.getItem_idItem());
            System.out.println("ID Prueba: " + dto.getPrueba_id_prueba());
            System.out.println("Valoración: " + dto.getValoracion());

            EvaluacionItem evaluacionItem = evaluacionItemMapper.toEntity(dto);
            
            // Asegurarse de que el ID de la prueba se está asignando
            evaluacionItem.setPrueba_idPrueba(dto.getPrueba_id_prueba());
            
            EvaluacionItem savedEvaluacionItem = evaluacionItemRepository.save(evaluacionItem);
            System.out.println("EvaluacionItem guardado con ID: " + savedEvaluacionItem.getIdEvaluacionItem());
            
            return evaluacionItemMapper.toDto(savedEvaluacionItem);
        } catch (Exception e) {
            System.err.println("Error al guardar EvaluacionItem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<DtoEvaluacionItem> saveAll(List<DtoEvaluacionItem> dtos) {
        try {
            System.out.println("Procesando lista de items: " + dtos);
            List<DtoEvaluacionItem> itemsGuardados = new ArrayList<>();
            
            for (DtoEvaluacionItem dto : dtos) {
                try {
                    // Validar los campos requeridos
                    if (dto.getEvaluacion_idEvaluacion() == null || 
                        dto.getItem_idItem() == null ||
                        dto.getPrueba_id_prueba() == null ||
                        dto.getValoracion() == null) {
                        throw new IllegalArgumentException("Todos los campos son requeridos");
                    }

                    DtoEvaluacionItem saved = save(dto);
                    itemsGuardados.add(saved);
                    System.out.println("Item guardado correctamente: " + saved);
                } catch (Exception e) {
                    System.err.println("Error guardando item: " + dto + "\nError: " + e.getMessage());
                    throw e;
                }
            }
            
            return itemsGuardados;
        } catch (Exception e) {
            System.err.println("Error en saveAll: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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
    public void delete(Integer id) {
        evaluacionItemRepository.deleteById(id);
    }

    // Método auxiliar para buscar por evaluación ID
    public List<DtoEvaluacionItem> findByEvaluacionId(Integer evaluacionId) {
        List<EvaluacionItem> items = evaluacionItemRepository.findByEvaluacionIdEvaluacion(evaluacionId);
        return evaluacionItemMapper.toDtoList(items);
    }
}
