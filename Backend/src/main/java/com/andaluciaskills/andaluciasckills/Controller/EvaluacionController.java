package com.andaluciaskills.andaluciasckills.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluancion;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchEvaluacionNoResultException;
import com.andaluciaskills.andaluciasckills.Service.EvaluacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluacion")
@RequiredArgsConstructor
public class EvaluacionController {
    
    private final EvaluacionService evaluacionService;

    @GetMapping
    public ResponseEntity<List<DtoEvaluancion>> getAllEvaluaciones() {
        List<DtoEvaluancion> result = evaluacionService.findAll();
        if (result.isEmpty()) {
            throw new SearchEvaluacionNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/BuscarEvaluacion/{id}")
    public ResponseEntity<DtoEvaluancion> getIdEvaluacion(@PathVariable Integer id) {
        return ResponseEntity.ok(
            evaluacionService.findById(id)
                .orElseThrow(() -> new EvaluacionNotFoundException())
        );
    }

    @PostMapping("/CrearEvaluacion")
    public ResponseEntity<DtoEvaluancion> createEvaluacion(@RequestBody DtoEvaluancion dto) {
        if (dto.getIdEvaluacion() != null) {
            throw new EvaluacionBadRequestException("No se puede crear una evaluacion con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(evaluacionService.save(dto));
    }

    @PutMapping("ModificarEvaluacion/{id}")
    public ResponseEntity<DtoEvaluancion> updateEvaluacion(
            @PathVariable Integer id, 
            @RequestBody DtoEvaluancion dto) {
        
        if (!id.equals(dto.getIdEvaluacion())) {
            throw new EvaluacionBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            evaluacionService.findById(id)
                .map(e -> {
                    dto.setIdEvaluacion(id);
                    return evaluacionService.save(dto);
                })
                .orElseThrow(() -> new EvaluacionNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarEvaluacion/{id}")
    public ResponseEntity<?> deleteEvaluacion(@PathVariable Integer id) {
        evaluacionService.findById(id)
            .orElseThrow(() -> new EvaluacionNotFoundException(id));
        
        evaluacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}