package com.andaluciaskills.andaluciasckills.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionItemBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionItemNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchEvaluacionItemNoResultException;
import com.andaluciaskills.andaluciasckills.Service.EvaluacionItemService;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/evaluacionItem")
public class EvaluacionItemController {

    private final EvaluacionItemService evaluacionItemService;

    public EvaluacionItemController(EvaluacionItemService evaluacionItemService) {
        this.evaluacionItemService = evaluacionItemService;
    }

    @GetMapping
    public ResponseEntity<List<DtoEvaluacionItem>> getAllEvaluacionItem() {
        List<DtoEvaluacionItem> result = evaluacionItemService.findAll();
        if (result.isEmpty()) {
            throw new SearchEvaluacionItemNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/BuscarEvaluacionItem/{id}")
    public ResponseEntity<DtoEvaluacionItem> getEvaluacionItem(@PathVariable Integer id) {
        return ResponseEntity.ok(
            evaluacionItemService.findById(id)
                .orElseThrow(() -> new EvaluacionItemNotFoundException())
        );
    }

    @PostMapping("/CrearEvaluacionItem")
    public ResponseEntity<DtoEvaluacionItem> createEvaluacionItem(@RequestBody DtoEvaluacionItem dto) {
        try {
            System.out.println("Recibiendo item para crear: " + dto);
            
            // Validar el item
            if (dto.getEvaluacion_idEvaluacion() == null) {
                throw new IllegalArgumentException("El ID de evaluación no puede ser null");
            }
            if (dto.getItem_idItem() == null) {
                throw new IllegalArgumentException("El ID del item no puede ser null");
            }
            if (dto.getPrueba_id_prueba() == null) {
                throw new IllegalArgumentException("El ID de la prueba no puede ser null");
            }
            if (dto.getValoracion() == null) {
                throw new IllegalArgumentException("La valoración no puede ser null");
            }

            DtoEvaluacionItem itemCreado = evaluacionItemService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(itemCreado);
        } catch (Exception e) {
            System.err.println("Error al crear item de evaluación: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear el item: " + e.getMessage());
        }
    }

    @PostMapping("/CrearEvaluacionItems")
    public ResponseEntity<List<DtoEvaluacionItem>> createEvaluacionItems(@RequestBody List<DtoEvaluacionItem> dtos) {
        try {
            System.out.println("Recibiendo items para crear: " + dtos);
            
            // Validar la lista
            if (dtos == null || dtos.isEmpty()) {
                throw new IllegalArgumentException("La lista de items no puede estar vacía");
            }

            // Validar cada item
            for (DtoEvaluacionItem dto : dtos) {
                if (dto.getEvaluacion_idEvaluacion() == null) {
                    throw new IllegalArgumentException("El ID de evaluación no puede ser null");
                }
                if (dto.getItem_idItem() == null) {
                    throw new IllegalArgumentException("El ID del item no puede ser null");
                }
                if (dto.getPrueba_id_prueba() == null) {
                    throw new IllegalArgumentException("El ID de la prueba no puede ser null");
                }
                if (dto.getValoracion() == null) {
                    throw new IllegalArgumentException("La valoración no puede ser null");
                }
            }

            List<DtoEvaluacionItem> itemsCreados = evaluacionItemService.saveAll(dtos);
            return ResponseEntity.status(HttpStatus.CREATED).body(itemsCreados);
        } catch (Exception e) {
            System.err.println("Error al crear items de evaluación: " + e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear los items: " + e.getMessage());
        }
    }

    @PutMapping("ModificarEvaluacionItem/{id}")
    public ResponseEntity<DtoEvaluacionItem> updateEvaluacionItem(
            @PathVariable Integer id, 
            @RequestBody DtoEvaluacionItem dto) {
        
        if (!id.equals(dto.getIdEvaluacionItem())) {
            throw new EvaluacionItemBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            evaluacionItemService.findById(id)
                .map(e -> {
                    dto.setIdEvaluacionItem(id);
                    return evaluacionItemService.save(dto);
                })
                .orElseThrow(() -> new EvaluacionItemNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarEvaluacionItem/{id}")
    public ResponseEntity<?> deleteEvaluacionItem(@PathVariable Integer id) {
        evaluacionItemService.findById(id)
            .orElseThrow(() -> new EvaluacionItemNotFoundException(id));
        evaluacionItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
