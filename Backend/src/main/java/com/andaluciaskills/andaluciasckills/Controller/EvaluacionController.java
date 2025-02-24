package com.andaluciaskills.andaluciasckills.Controller;

import java.util.List;
import java.util.Map;

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

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacion;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchEvaluacionNoResultException;
import com.andaluciaskills.andaluciasckills.Service.EvaluacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {
    
    private final EvaluacionService evaluacionService;

    @GetMapping("/ListarEvaluaciones")
    public ResponseEntity<List<DtoEvaluacion>> getAllEvaluaciones() {
        List<DtoEvaluacion> result = evaluacionService.findAll();
        if (result.isEmpty()) {
            throw new SearchEvaluacionNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/BuscarEvaluacion/{id}")
    public ResponseEntity<DtoEvaluacion> getIdEvaluacion(@PathVariable Integer id) {
        return ResponseEntity.ok(
            evaluacionService.findById(id)
                .orElseThrow(() -> new EvaluacionNotFoundException(id))
        );
    }

    @PostMapping("/ ")
    public ResponseEntity<DtoEvaluacion> createEvaluacion(@RequestBody DtoEvaluacion dto) {
        if (dto.getIdEvaluacion() != null) {
            throw new EvaluacionBadRequestException("No se puede crear una evaluación con un ID ya existente");
        }
        try {
            DtoEvaluacion evaluacionCreada = evaluacionService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(evaluacionCreada);
        } catch (Exception e) {
            throw new EvaluacionBadRequestException("Error al crear la evaluación: " + e.getMessage());
        }
    }

    @PutMapping("/ModificarEvaluacion/{id}")
    public ResponseEntity<DtoEvaluacion> updateEvaluacion(
            @PathVariable Integer id, 
            @RequestBody DtoEvaluacion dto) {
        
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

    @DeleteMapping("/BorrarEvaluacion/{id}")
    public ResponseEntity<?> deleteEvaluacion(@PathVariable Integer id) {
        evaluacionService.findById(id)
            .orElseThrow(() -> new EvaluacionNotFoundException(id));
        
        evaluacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ganadores")
    public ResponseEntity<List<Map<String, Object>>> obtenerGanadores() {
        try {
            List<Map<String, Object>> ganadores = evaluacionService.obtenerGanadores();
            if (ganadores.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(ganadores);
        } catch (Exception e) {
            System.err.println("Error al obtener ganadores: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(List.of(Map.of(
                    "error", "Error al obtener los ganadores",
                    "mensaje", e.getMessage()
                )));
        }
    }
}