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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
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
    public ResponseEntity<List<Evaluacion>> getAllEspecialidades() {
        List<Evaluacion> result = evaluacionService.findAll();
        if (result.isEmpty()) {
            throw new SearchEvaluacionNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/BuscarEvaluacion/{id}")
    public ResponseEntity<Evaluacion> getEvaluacion(@RequestParam Integer id) {
        return ResponseEntity.ok(
            evaluacionService.findById(id)
                .orElseThrow(() -> new SearchEvaluacionNoResultException())
        );
    }

    @PostMapping("/CrearEvaluacion")
    public ResponseEntity<Evaluacion> createEspecialidad(@RequestBody Evaluacion evaluacion) {
        if (evaluacion.getIdEvaluacion() != null) {
            throw new EvaluacionBadRequestException("No se puede crear una especialidad con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(evaluacionService.save(evaluacion));
    }

    @PutMapping("ModificarEvaluacion/{id}")
    public ResponseEntity<Evaluacion> updateEvaluacion(
            @PathVariable Integer id, 
            @RequestBody Evaluacion evaluacion) {
        
        if (!id.equals(evaluacion.getIdEvaluacion())) {
            throw new EvaluacionBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            evaluacionService.findById(id)
                .map(e -> {
                    evaluacion.setIdEvaluacion(id);
                    return evaluacionService.save(evaluacion);
                })
                .orElseThrow(() -> new EvaluacionNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarEvaluacion/{id}")
    public ResponseEntity<Evaluacion> deleteEvaluacion(@PathVariable Integer id) {
        Evaluacion evaluacion = evaluacionService.findById(id)
                .orElseThrow(() -> new EvaluacionNotFoundException(id));
        
        evaluacionService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}