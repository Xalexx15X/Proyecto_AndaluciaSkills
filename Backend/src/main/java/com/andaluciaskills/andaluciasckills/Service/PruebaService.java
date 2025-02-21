package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Entity.Item;
import com.andaluciaskills.andaluciasckills.Entity.Prueba;
import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Error.UserNotFoundException;
import com.andaluciaskills.andaluciasckills.Mapper.PruebaMapper;
import com.andaluciaskills.andaluciasckills.Repository.PruebaRepository;
import com.andaluciaskills.andaluciasckills.Repository.UserRepository;
import com.andaluciaskills.andaluciasckills.Repository.ItemRepository;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionRepository;
import com.andaluciaskills.andaluciasckills.Repository.EvaluacionItemRepository;
import com.andaluciaskills.andaluciasckills.Service.base.PruebaBaseService;

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
    private final UserRepository userRepository;

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
            Prueba prueba = pruebaMapper.toEntity(dtoPrueba);
            Prueba pruebaGuardada = pruebaRepository.save(prueba);
            System.out.println("Prueba creada con ID: " + pruebaGuardada.getIdPrueba());

            // 2. Crear los items
            List<Item> items = dtoItems.stream().map(dtoItem -> {
                Item item = new Item();
                item.setDescripcion(dtoItem.getDescripcion());
                item.setPeso(dtoItem.getPeso());
                item.setGradosConsecucion(dtoItem.getGradosConsecucion());
                item.setPruebaIdPrueba(pruebaGuardada.getIdPrueba());
                return item;
            }).collect(Collectors.toList());
            
            List<Item> itemsGuardados = itemRepository.saveAll(items);
            System.out.println("Items creados con IDs: " + 
                itemsGuardados.stream()
                    .map(item -> item.getIdItem().toString())
                    .collect(Collectors.joining(", ")));

            // 3. Buscar el User y crear evaluación
            User user = userRepository.findByNombreAndApellidos(
                dtoPrueba.getNombreUser(), 
                dtoPrueba.getApellidosUser()
            ).orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
            
            Evaluacion evaluacion = new Evaluacion();
            evaluacion.setNotaFinal(0.0);
            evaluacion.setParticipante_idParticipante(dtoPrueba.getParticipante_idParticipante());
            evaluacion.setPrueba_idPrueba(pruebaGuardada.getIdPrueba());
            evaluacion.setUser_idUser(user.getIdUser());
            
            Evaluacion evaluacionGuardada = evaluacionRepository.save(evaluacion);
            System.out.println("Evaluación creada con ID: " + evaluacionGuardada.getIdEvaluacion());

            // 4. Crear los evaluacionItems
            List<EvaluacionItem> evaluacionItems = itemsGuardados.stream().map(item -> {
                EvaluacionItem evaluacionItem = new EvaluacionItem();
                evaluacionItem.setValoracion(0);
                evaluacionItem.setEvaluacion_idEvaluacion(evaluacionGuardada.getIdEvaluacion());
                evaluacionItem.setItem_idItem(item.getIdItem());
                evaluacionItem.setPrueba_idPrueba(pruebaGuardada.getIdPrueba());
                return evaluacionItem;
            }).collect(Collectors.toList());

            List<EvaluacionItem> evaluacionItemsGuardados = evaluacionItemRepository.saveAll(evaluacionItems);
            System.out.println("EvaluacionItems creados con IDs: " + 
                evaluacionItemsGuardados.stream()
                    .map(ei -> ei.getIdEvaluacionItem().toString())
                    .collect(Collectors.joining(", ")));

            return pruebaMapper.toDto(pruebaGuardada);
        } catch (Exception e) {
            System.err.println("Error en crearPruebaConItems: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}