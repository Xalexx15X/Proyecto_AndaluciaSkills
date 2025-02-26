package com.andaluciaskills.andaluciasckills.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andaluciaskills.andaluciasckills.Dto.DtoEspecialidades;
import com.andaluciaskills.andaluciasckills.Error.*;
import com.andaluciaskills.andaluciasckills.Service.EspecialidadService;

import lombok.RequiredArgsConstructor;
import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
@Tag(name = "Especialidades", description = "API para la gestión de especialidades")
public class EspecialidadController{
    
    private final EspecialidadService especialidadService;

    @Operation(
        summary = "Obtener todas las especialidades",
        description = "Recupera la lista completa de especialidades disponibles"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de especialidades recuperada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontraron especialidades")
    })
    @GetMapping
    public ResponseEntity<List<DtoEspecialidades>> getAllEspecialidades() {
        List<DtoEspecialidades> result = especialidadService.findAll();
        if (result.isEmpty()) {
            throw new SearchEspecialidadNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Buscar especialidad por ID",
        description = "Recupera una especialidad específica mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Especialidad encontrada"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    @GetMapping("/BuscarEspecialidad/{id}")
    public ResponseEntity<DtoEspecialidades> getEspecialidad(@PathVariable Integer id) {
        return ResponseEntity.ok(
            especialidadService.findById(id)
                .orElseThrow(() -> new EspecialidadNotFoundException())
        );
    }

    @Operation(
        summary = "Crear nueva especialidad",
        description = "Crea una nueva especialidad con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Especialidad creada correctamente",
            content = @Content(schema = @Schema(implementation = DtoEspecialidades.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de especialidad inválidos"
        )
    })
    @PostMapping("/CrearEspecialidad")
    public ResponseEntity<?> crearEspecialidad(@Valid @RequestBody DtoEspecialidades dto) {
        if (dto.getIdEspecialidad() != null) {
            throw new EspecialidadBadRequestException("No se puede crear una especialidad con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(especialidadService.save(dto));
    }

    @Operation(
        summary = "Modificar especialidad existente",
        description = "Actualiza los datos de una especialidad existente"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Especialidad actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de especialidad inválidos"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    @PutMapping("ModificarEspecialidad/{id}")
    public ResponseEntity<DtoEspecialidades> updateEspecialidad(
            @PathVariable Integer id, 
            @Valid @RequestBody DtoEspecialidades dto) {
        
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

    @Operation(
        summary = "Eliminar especialidad",
        description = "Elimina una especialidad existente mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Especialidad eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Especialidad no encontrada")
    })
    @DeleteMapping("BorrarEspecialidad/{id}")
    public ResponseEntity<?> deleteEspecialidad(@PathVariable Integer id) {
        especialidadService.findById(id)
            .orElseThrow(() -> new EspecialidadNotFoundException(id));
        especialidadService.delete(id);
        return ResponseEntity.noContent().build();
    }
}