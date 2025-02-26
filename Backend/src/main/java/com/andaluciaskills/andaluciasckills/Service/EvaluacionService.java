package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacion;
import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Mapper.EvaluacionMapper;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionRepository;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.EvaluacionBaseService;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service // Marca la clase como un servicio de Spring
public class EvaluacionService implements EvaluacionBaseService {
    private final EvaluacionRepository evaluacionRepository; // Para operaciones en BD
    private final EvaluacionMapper evaluacionMapper; // Para convertir entre Entity y DTO
    private final EvaluacionItemRepository evaluacionItemRepository; // Para items de evaluación
    private final ItemService itemService; // Para obtener información de los items

    public EvaluacionService(EvaluacionRepository evaluacionRepository, EvaluacionMapper evaluacionMapper, EvaluacionItemRepository evaluacionItemRepository, ItemService itemService) 
    { 
        this.evaluacionRepository = evaluacionRepository;
        this.evaluacionMapper = evaluacionMapper;
        this.evaluacionItemRepository = evaluacionItemRepository;
        this.itemService = itemService;
    }

    public Double calcularNotaFinal(Integer evaluacionId) {
        // 1. Obtiene todos los items de la evaluación
        List<EvaluacionItem> items = evaluacionItemRepository.findByEvaluacionIdEvaluacion(evaluacionId);
        
        // 2. Si no hay items, devuelve 0
        if (items.isEmpty()) {
            return 0.0;
        }
        
        double notaFinal = 0.0;
        double sumaPesos = 0.0;

        // 3. Para cada item de evaluación
        for (EvaluacionItem evaluacionItem : items) {
            if (evaluacionItem.getValoracion() != null) {
                // 4. Obtiene la información del item
                Optional<DtoItem> item = itemService.findById(evaluacionItem.getItem_idItem());
                
                if (item.isPresent()) {
                    // 5. Obtiene los valores necesarios
                    Integer peso = item.get().getPeso(); // Ejemplo: 30 para 30%
                    Double puntuacionMaxima = item.get().getPuntuacionMaxima();
                    Double valoracion = evaluacionItem.getValoracion();
                    
                    // 6. Calcula la nota ponderada
                    double notaPonderada = (valoracion / puntuacionMaxima) * peso;
                    notaFinal += notaPonderada;
                    sumaPesos += peso;
                    
                    // 7. Logs para debugging
                    System.out.println("Item: " + item.get().getDescripcion());
                    System.out.println("Peso: " + peso + "%");
                    System.out.println("Puntuación máxima: " + puntuacionMaxima);
                    System.out.println("Valoración: " + valoracion);
                    System.out.println("Nota ponderada: " + notaPonderada);
                }
            }
        }
        
        // 8. Normaliza la nota final
        if (sumaPesos > 0) {
            notaFinal = (notaFinal / sumaPesos) * 100;
        }
        
        // 9. Redondea a 2 decimales
        return Math.round(notaFinal * 100.0) / 100.0;
    }

    public void actualizarNotaFinal(Integer evaluacionId) {
        // 1. Calcula la nueva nota final
        Double notaFinal = calcularNotaFinal(evaluacionId);
        
        // 2. Busca la evaluación y actualiza su nota
        evaluacionRepository.findById(evaluacionId).ifPresent(evaluacion -> {
            evaluacion.setNotaFinal(notaFinal);
            evaluacionRepository.save(evaluacion);
        });
    }

    @Override
    public DtoEvaluacion save(DtoEvaluacion dto) {
        try {
            // 1. Convierte DTO a entidad
            Evaluacion evaluacion = evaluacionMapper.toEntity(dto);
            
            // 2. Guarda la evaluación
            Evaluacion savedEvaluacion = evaluacionRepository.save(evaluacion);
            
            // 3. Calcula y actualiza la nota final
            Double notaFinal = calcularNotaFinal(savedEvaluacion.getIdEvaluacion());
            savedEvaluacion.setNotaFinal(notaFinal);
            
            // 4. Guarda de nuevo con la nota final actualizada
            savedEvaluacion = evaluacionRepository.save(savedEvaluacion);
            
            // 5. Convierte y devuelve el resultado
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
            Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));

            // Obtener todos los items de la evaluación
            List<EvaluacionItem> evaluacionItems = evaluacionItemRepository.findByEvaluacionIdEvaluacion(evaluacionId);
            
            if (evaluacionItems.isEmpty()) {
                throw new RuntimeException("No se encontraron items para la evaluación");
            }

            double sumaPonderada = 0.0;
            double sumaPesos = 0.0;

            for (EvaluacionItem evaluacionItem : evaluacionItems) {
                // Obtener el Item usando ItemService
                Optional<DtoItem> itemOpt = itemService.findById(evaluacionItem.getItem_idItem());
                
                if (itemOpt.isPresent()) {
                    DtoItem item = itemOpt.get();
                    double pesoItem = item.getPeso(); // El peso es el porcentaje (ej: 30 significa 30%)
                    double valoracion = evaluacionItem.getValoracion();
                    
                    // Si el peso es 30%, el máximo que puede poner es 3
                    // Convertimos la valoración a una escala sobre 10
                    double valoracionSobre10 = (valoracion * 10) / (pesoItem / 10);
                    
                    System.out.println("Item ID: " + item.getIdItem());
                    System.out.println("Peso: " + pesoItem + "%");
                    System.out.println("Valoración original: " + valoracion);
                    System.out.println("Valoración sobre 10: " + valoracionSobre10);
                    
                    sumaPonderada += (valoracionSobre10 * pesoItem);
                    sumaPesos += pesoItem;
                }
            }

            // Calcular nota final sobre 10
            double notaFinal = (sumaPesos > 0) ? (sumaPonderada / sumaPesos) : 0.0;
            
            // Redondear a 2 decimales
            notaFinal = Math.round(notaFinal * 100.0) / 100.0;
            
            // Guardar la nota final
            evaluacion.setNotaFinal(notaFinal);
            Evaluacion evaluacionActualizada = evaluacionRepository.save(evaluacion);
            
            System.out.println("Nota final calculada: " + notaFinal);
            
            return evaluacionMapper.toDto(evaluacionActualizada);
            
        } catch (Exception e) {
            System.err.println("Error al calcular nota final: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al calcular la nota final", e);
        }
    }

    public List<Map<String, Object>> obtenerGanadores() {
        // 1. Obtiene todos los resultados de la base de datos
        List<Map<String, Object>> todosLosResultados = evaluacionRepository.findGanadoresPorEspecialidad();
        
        // 2. Crea un mapa para almacenar solo el mejor de cada especialidad
        Map<String, Map<String, Object>> ganadoresPorEspecialidad = new HashMap<>();
        
        // 3. Procesa cada resultado
        for (Map<String, Object> resultado : todosLosResultados) {
            String especialidad = (String) resultado.get("especialidad");
            Double notaMedia = (Double) resultado.get("notaMedia");
            
            // 4. Si no hay ganador para esta especialidad o este tiene mejor nota
            if (!ganadoresPorEspecialidad.containsKey(especialidad) || 
                (Double) ganadoresPorEspecialidad.get(especialidad).get("notaMedia") < notaMedia) {
                ganadoresPorEspecialidad.put(especialidad, resultado);
            }
        }
        
        // 5. Devuelve solo los ganadores
        return new ArrayList<>(ganadoresPorEspecialidad.values());
    }
}