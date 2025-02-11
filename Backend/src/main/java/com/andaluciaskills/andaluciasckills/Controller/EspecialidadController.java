package com.andaluciaskills.andaluciasckills.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andaluciaskills.andaluciasckills.Entity.Especialidad;
import com.andaluciaskills.andaluciasckills.Error.*;
import com.andaluciaskills.andaluciasckills.Service.EspecialidadService;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadController{
    
    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<Especialidad>> getAllEspecialidades() {
        List<Especialidad> result = especialidadService.findAll();
        if (result.isEmpty()) {
            throw new SearchEspecialidadNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/BuscarEspecialidad/{id}")
    public ResponseEntity<Especialidad> getEspecialidad(@PathVariable Integer id) {
        return ResponseEntity.ok(
            especialidadService.findById(id)
                .orElseThrow(() -> new EspecialidadNotFoundException())
        );
    }

    @PostMapping("/CrearEspecialidad")
    public ResponseEntity<Especialidad> createEspecialidad(@RequestBody Especialidad especialidad) {
        if (especialidad.getIdEspecialidad() != null) {
            throw new EspecialidadBadRequestException("No se puede crear una especialidad con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(especialidadService.save(especialidad));
    }

    @PutMapping("ModificarEspecialidad/{id}")
    public ResponseEntity<Especialidad> updateEspecialidad(
            @PathVariable Integer id, 
            @RequestBody Especialidad especialidad) {
        
        if (!id.equals(especialidad.getIdEspecialidad())) {
            throw new EspecialidadBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            especialidadService.findById(id)
                .map(e -> {
                    especialidad.setIdEspecialidad(id);
                    return especialidadService.save(especialidad);
                })
                .orElseThrow(() -> new EspecialidadNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarEspecialidad/{id}")
    public ResponseEntity<Especialidad> deleteEspecialidad(@PathVariable Integer id) {
        Especialidad especialidad = especialidadService.findById(id)
                .orElseThrow(() -> new EspecialidadNotFoundException(id));
        
        especialidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}