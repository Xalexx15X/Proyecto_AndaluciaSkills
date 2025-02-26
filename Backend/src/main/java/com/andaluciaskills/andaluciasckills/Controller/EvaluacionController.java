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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
@Tag(name = "Evaluaciones", description = "API para la gestión de evaluaciones de participantes")
public class EvaluacionController {
    
    private final EvaluacionService evaluacionService;

    @Operation(
        summary = "Listar todas las evaluaciones",
        description = "Obtiene una lista de todas las evaluaciones registradas en el sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de evaluaciones recuperada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones")
    })
    @GetMapping("/ListarEvaluaciones")
    public ResponseEntity<List<DtoEvaluacion>> getAllEvaluaciones() {
        List<DtoEvaluacion> result = evaluacionService.findAll();
        if (result.isEmpty()) {
            throw new SearchEvaluacionNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Buscar evaluación por ID",
        description = "Recupera una evaluación específica mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluación encontrada"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    @GetMapping("/BuscarEvaluacion/{id}")
    public ResponseEntity<DtoEvaluacion> getIdEvaluacion(@PathVariable Integer id) {
        return ResponseEntity.ok(
            evaluacionService.findById(id)
                .orElseThrow(() -> new EvaluacionNotFoundException(id))
        );
    }

    @Operation(
        summary = "Crear nueva evaluación",
        description = "Crea una nueva evaluación con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Evaluación creada correctamente",
            content = @Content(schema = @Schema(implementation = DtoEvaluacion.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de evaluación inválidos o ID ya existente"
        )
    })
    @PostMapping("/CrearEvaluacion")
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

    @Operation(
        summary = "Modificar evaluación",
        description = "Actualiza los datos de una evaluación existente"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluación actualizada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de evaluación inválidos"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
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

    @Operation(
        summary = "Eliminar evaluación",
        description = "Elimina una evaluación existente mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evaluación eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Evaluación no encontrada")
    })
    @DeleteMapping("/BorrarEvaluacion/{id}")
    public ResponseEntity<?> deleteEvaluacion(@PathVariable Integer id) {
        evaluacionService.findById(id)
            .orElseThrow(() -> new EvaluacionNotFoundException(id));
        
        evaluacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener ganadores",
        description = "Recupera la lista de ganadores por especialidad con sus puntuaciones"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200", 
            description = "Lista de ganadores recuperada con éxito"
        ),
        @ApiResponse(
            responseCode = "204", 
            description = "No se encontraron ganadores"
        ),
        @ApiResponse(
            responseCode = "500", 
            description = "Error interno al procesar la solicitud"
        )
    })
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