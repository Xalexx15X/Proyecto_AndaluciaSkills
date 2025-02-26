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

@Service // Indica que esta clase es un servicio de Spring
@RequiredArgsConstructor // Crea un constructor con los campos final automáticamente
public class EvaluacionItemService implements EvaluacionItemBaseService {
    // Dependencias necesarias inyectadas por constructor
    private final EvaluacionItemRepository evaluacionItemRepository; // Para operaciones con la base de datos
    private final EvaluacionItemMapper evaluacionItemMapper; // Para convertir entre Entity y DTO
    private final EvaluacionService evaluacionService; // Para actualizar notas finales

    @Override
    public DtoEvaluacionItem save(DtoEvaluacionItem dto) {
        try {
            // Log para debugging
            System.out.println("Guardando EvaluacionItem: " + dto);
            
            // Validación de campos obligatorios
            if (dto.getEvaluacion_idEvaluacion() == null) {
                throw new IllegalArgumentException("El ID de evaluación no puede ser null");
            }
            if (dto.getItem_idItem() == null) {
                throw new IllegalArgumentException("El ID del item no puede ser null");
            }
            if (dto.getPrueba_id_prueba() == null) {
                throw new IllegalArgumentException("El ID de la prueba no puede ser null");
            }

            // Logs adicionales para verificar valores
            System.out.println("ID Evaluación: " + dto.getEvaluacion_idEvaluacion());
            System.out.println("ID Item: " + dto.getItem_idItem());
            System.out.println("ID Prueba: " + dto.getPrueba_id_prueba());
            System.out.println("Valoración: " + dto.getValoracion());

            // Convertir DTO a entidad
            EvaluacionItem evaluacionItem = evaluacionItemMapper.toEntity(dto);
            
            // Asegurar que el ID de prueba se asigne correctamente
            evaluacionItem.setPrueba_idPrueba(dto.getPrueba_id_prueba());
            
            // Guardar en la base de datos
            EvaluacionItem savedEvaluacionItem = evaluacionItemRepository.save(evaluacionItem);
            
            // Convertir la entidad guardada de vuelta a DTO y devolverla
            return evaluacionItemMapper.toDto(savedEvaluacionItem);
        } catch (Exception e) {
            // Manejo de errores con logs
            System.err.println("Error al guardar EvaluacionItem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Método para guardar múltiples items de evaluación a la vez
    public List<DtoEvaluacionItem> saveAll(List<DtoEvaluacionItem> dtos) {
        try {
            List<DtoEvaluacionItem> itemsGuardados = new ArrayList<>();
            
            // Procesar cada item de la lista
            for (DtoEvaluacionItem dto : dtos) {
                try {
                    // Validar que todos los campos requeridos estén presentes
                    if (dto.getEvaluacion_idEvaluacion() == null || 
                        dto.getItem_idItem() == null ||
                        dto.getPrueba_id_prueba() == null ||
                        dto.getValoracion() == null) {
                        throw new IllegalArgumentException("Todos los campos son requeridos");
                    }

                    // Guardar cada item individualmente
                    DtoEvaluacionItem saved = save(dto);
                    itemsGuardados.add(saved);
                } catch (Exception e) {
                    System.err.println("Error guardando item: " + dto);
                    throw e;
                }
            }
            
            return itemsGuardados;
        } catch (Exception e) {
            System.err.println("Error en saveAll: " + e.getMessage());
            throw e;
        }
    }

    // Buscar un item de evaluación por su ID
    @Override
    public Optional<DtoEvaluacionItem> findById(Integer id) {
        return evaluacionItemRepository.findById(id)
                .map(evaluacionItemMapper::toDto);
    }

    // Obtener todos los items de evaluación
    @Override
    public List<DtoEvaluacionItem> findAll() {
        return evaluacionItemMapper.toDtoList(evaluacionItemRepository.findAll());
    }

    // Actualizar un item de evaluación existente
    @Override
    public DtoEvaluacionItem update(DtoEvaluacionItem dto) {
        // Verificar que el item tenga ID
        if (dto.getIdEvaluacionItem() == null) {
            throw new IllegalArgumentException("No se puede actualizar un EvaluacionItem sin ID");
        }
        
        // Convertir, guardar y actualizar nota final
        EvaluacionItem evaluacionItem = evaluacionItemMapper.toEntity(dto);
        EvaluacionItem updatedEvaluacionItem = evaluacionItemRepository.save(evaluacionItem);
        evaluacionService.actualizarNotaFinal(dto.getEvaluacion_idEvaluacion());
        
        return evaluacionItemMapper.toDto(updatedEvaluacionItem);
    }

    // Eliminar un item de evaluación por su ID
    @Override
    public void delete(Integer id) {
        evaluacionItemRepository.deleteById(id);
    }

    // Buscar todos los items de una evaluación específica
    public List<DtoEvaluacionItem> findByEvaluacionId(Integer evaluacionId) {
        List<EvaluacionItem> items = evaluacionItemRepository.findByEvaluacionIdEvaluacion(evaluacionId);
        return evaluacionItemMapper.toDtoList(items);
    }
}