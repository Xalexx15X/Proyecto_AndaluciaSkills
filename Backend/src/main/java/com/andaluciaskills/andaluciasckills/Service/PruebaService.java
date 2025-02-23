package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacion;
import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;
import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Entity.Prueba;
import com.andaluciaskills.andaluciasckills.Mapper.EvaluacionMapper;
import com.andaluciaskills.andaluciasckills.Mapper.ItemMapper;
import com.andaluciaskills.andaluciasckills.Mapper.PruebaMapper;
import com.andaluciaskills.andaluciasckills.Repository.PruebaRepository;
import com.andaluciaskills.andaluciasckills.Repository.ItemRepository;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionRepository;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.PruebaBaseService;

import com.andaluciaskills.andaluciasckills.Entity.Item;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PruebaService implements PruebaBaseService {
    private final PruebaRepository pruebaRepository;
    private final PruebaMapper pruebaMapper;
    private final ItemRepository itemRepository;
    private final EvaluacionRepository evaluacionRepository;
    private final EvaluacionItemRepository evaluacionItemRepository;
    private final ItemMapper itemMapper;
    private final EvaluacionMapper evaluacionMapper;
    private final EvaluacionService evaluacionService;

    @Override
    public DtoPrueba save(DtoPrueba dto) {
        Prueba prueba = pruebaMapper.toEntity(dto);
        Prueba savedPrueba = pruebaRepository.save(prueba);
        return pruebaMapper.toDto(savedPrueba);
    }

    @Override
    public Optional<DtoPrueba> findById(Integer id) {
        return pruebaRepository.findById(id)
                .map(pruebaMapper::toDto);
    }

    @Override
    public List<DtoPrueba> findAll() {
        return pruebaMapper.toDtoList(pruebaRepository.findAll());
    }

    @Override
    public DtoPrueba update(DtoPrueba dto) {
        Prueba prueba = pruebaMapper.toEntity(dto);
        Prueba updatedPrueba = pruebaRepository.save(prueba);
        return pruebaMapper.toDto(updatedPrueba);
    }

    @Override
    public void delete(Integer id) {
        pruebaRepository.deleteById(id);
    }

    @Transactional
    public DtoPrueba crearPruebaConItems(DtoPrueba dtoPrueba, List<DtoItem> dtoItems) {
        try {
            // 1. Crear la prueba
            DtoPrueba pruebaGuardada = save(dtoPrueba);
            System.out.println("Prueba creada con ID: " + pruebaGuardada.getIdPrueba());

            // 2. Crear los items
            dtoItems.forEach(item -> {
                item.setPrueba_id_Prueba(pruebaGuardada.getIdPrueba());
            });
            List<DtoItem> itemsGuardados = itemRepository.saveAll(dtoItems.stream()
                .map(itemMapper::toEntity)
                .collect(Collectors.toList()))
                .stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());

            System.out.println("Items creados con IDs: " + 
                itemsGuardados.stream()
                    .map(item -> item.getIdItem().toString())
                    .collect(Collectors.joining(", ")));

            return pruebaGuardada;
        } catch (Exception e) {
            System.err.println("Error en crearPruebaConItems: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public DtoEvaluacion crearEvaluacionParaPrueba(Integer pruebaId, Integer participanteId, Integer userId) {
        try {
            System.out.println("Creando evaluación con: pruebaId=" + pruebaId + 
                              ", participanteId=" + participanteId + 
                              ", userId=" + userId);

            DtoEvaluacion evaluacion = new DtoEvaluacion();
            evaluacion.setPrueba_idPrueba(pruebaId);
            evaluacion.setParticipante_idParticipante(participanteId);
            evaluacion.setUser_idUser(userId);
            evaluacion.setNotaFinal(0.0);

            // Guardar la evaluación
            DtoEvaluacion evaluacionGuardada = evaluacionService.save(evaluacion);
            System.out.println("Evaluación guardada: " + evaluacionGuardada);
            
            return evaluacionGuardada;
        } catch (Exception e) {
            System.err.println("Error en crearEvaluacionParaPrueba: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<DtoPrueba> findByEspecialidadId(Integer especialidadId) {
        List<Prueba> pruebas = pruebaRepository.findByEspecialidadId(especialidadId);
        return pruebaMapper.toDtoList(pruebas);
    }

    @Transactional
    public DtoEvaluacion actualizarNotasEvaluacion(Integer evaluacionId, List<DtoEvaluacionItem> evaluacionItems) {
        try {
            System.out.println("Procesando evaluacionItems: " + evaluacionItems);
            
            // Verificar si ya existe una evaluación
            Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));
                
            if (evaluacionItemRepository.existsById(evaluacionId)) {
                throw new RuntimeException("Esta evaluación ya tiene items evaluados");
            }
            
            // 1. Guardar cada EvaluacionItem
            List<EvaluacionItem> evaluacionesGuardadas = evaluacionItems.stream()
                .map(dto -> {
                    EvaluacionItem entity = new EvaluacionItem();
                    entity.setValoracion(dto.getValoracion());
                    entity.setEvaluacion_idEvaluacion(evaluacionId);
                    entity.setItem_idItem(dto.getItem_idItem());
                    return evaluacionItemRepository.save(entity);
                })
                .collect(Collectors.toList());

            // 2. Calcular nota final
            double notaFinal = evaluacionesGuardadas.stream()
                .mapToDouble(ei -> {
                    Item item = itemRepository.findById(ei.getItem_idItem())
                        .orElseThrow(() -> new RuntimeException("Item no encontrado"));
                    return (ei.getValoracion() * item.getPeso()) / 100.0;
                })
                .sum();

            // 3. Actualizar la nota final
            evaluacion.setNotaFinal(notaFinal);
            evaluacion = evaluacionRepository.save(evaluacion);
            
            return evaluacionMapper.toDto(evaluacion);

        } catch (Exception e) {
            System.err.println("Error al actualizar notas: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public boolean existeEvaluacion(Integer pruebaId, Integer participanteId) {
        return evaluacionRepository.existsByPruebaIdAndParticipanteId(pruebaId, participanteId
        );
    }
}