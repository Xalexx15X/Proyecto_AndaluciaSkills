package com.andaluciaskills.andaluciasckills.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andaluciaskills.andaluciasckills.Dto.DtoEspecialidades;
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
    public ResponseEntity<List<DtoEspecialidades>> getAllEspecialidades() {
        List<DtoEspecialidades> result = especialidadService.findAll();
        if (result.isEmpty()) {
            throw new SearchEspecialidadNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/BuscarEspecialidad/{id}")
    public ResponseEntity<DtoEspecialidades> getEspecialidad(@PathVariable Integer id) {
        return ResponseEntity.ok(
            especialidadService.findById(id)
                .orElseThrow(() -> new EspecialidadNotFoundException())
        );
    }

    @PostMapping("/CrearEspecialidad")
    public ResponseEntity<DtoEspecialidades> createEspecialidad(@RequestBody DtoEspecialidades dto) {
        if (dto.getIdEspecialidad() != null) {
            throw new EspecialidadBadRequestException("No se puede crear una especialidad con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(especialidadService.save(dto));
    }

    @PutMapping("ModificarEspecialidad/{id}")
    public ResponseEntity<DtoEspecialidades> updateEspecialidad(
            @PathVariable Integer id, 
            @RequestBody DtoEspecialidades dto) {
        
        if (!id.equals(dto.getIdEspecialidad())) {
            throw new EspecialidadBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            especialidadService.findById(id)
                .map(e -> {
                    dto.setIdEspecialidad(id);
                    return especialidadService.save(dto);
                })
                .orElseThrow(() -> new EspecialidadNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarEspecialidad/{id}")
    public ResponseEntity<?> deleteEspecialidad(@PathVariable Integer id) {
        especialidadService.findById(id)
            .orElseThrow(() -> new EspecialidadNotFoundException(id));
        
        especialidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}