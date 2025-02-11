package com.andaluciaskills.andaluciasckills.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andaluciaskills.andaluciasckills.Entity.EvaluacionItem;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionItemBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionItemNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchEvaluacionItemNoResultException;
import com.andaluciaskills.andaluciasckills.Service.EvaluacionItemService;

import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class EvaluacionItemController {
    private final EvaluacionItemService evaluacionItemService;

    @GetMapping
    public ResponseEntity<List<EvaluacionItem>> getAllEspecialidades() {
        List<EvaluacionItem> result = evaluacionItemService.findAll();
        if (result.isEmpty()) {
            throw new SearchEvaluacionItemNoResultException();
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/BuscarEvaluacionItem/{id}")
    public ResponseEntity<EvaluacionItem> getEvaluacion(@RequestParam Integer id) {
        return ResponseEntity.ok(
            evaluacionItemService.findById(id)
                .orElseThrow(() -> new SearchEvaluacionItemNoResultException())
        );
    }

    @PostMapping("/CrearEvaluacionItem")
    public ResponseEntity<EvaluacionItem> createEspecialidad(@RequestBody EvaluacionItem evaluacionItem) {
        if (evaluacionItem.getIdEvaluacionItem() != null) {
            throw new EvaluacionItemBadRequestException("No se puede crear una especialidad con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(evaluacionItemService.save(evaluacionItem));
    }

    @PutMapping("ModificarEvaluacionItem/{id}")
    public ResponseEntity<EvaluacionItem> updateEvaluacionItem(
            @PathVariable Integer id, 
            @RequestBody EvaluacionItem evaluacionItem) {
        
        if (!id.equals(evaluacionItem.getIdEvaluacionItem())) {
            throw new EvaluacionItemBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            evaluacionItemService.findById(id)
                .map(e -> {
                    evaluacionItem.setIdEvaluacionItem(id);
                    return evaluacionItemService.save(evaluacionItem);
                })
                .orElseThrow(() -> new EvaluacionItemNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarEvaluacionItem/{id}")
    public ResponseEntity<EvaluacionItem> deleteEvaluacionItem(@PathVariable Integer id) {
        EvaluacionItem evaluacionItem = evaluacionItemService.findById(id)
                .orElseThrow(() -> new EvaluacionItemNotFoundException(id));
        
        evaluacionItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
