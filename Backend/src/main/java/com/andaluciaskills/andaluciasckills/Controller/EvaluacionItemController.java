package com.andaluciaskills.andaluciasckills.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionItemBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.EvaluacionItemNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchEvaluacionItemNoResultException;
import com.andaluciaskills.andaluciasckills.Service.EvaluacionItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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
@Tag(name = "Evaluación Items", description = "API para la gestión de items de evaluación")
public class EvaluacionItemController {

    private final EvaluacionItemService evaluacionItemService;

    public EvaluacionItemController(EvaluacionItemService evaluacionItemService) {
        this.evaluacionItemService = evaluacionItemService;
    }

    @Operation(
        summary = "Listar todos los items de evaluación",
        description = "Obtiene una lista de todos los items de evaluación registrados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de items recuperada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontraron items")
    })
    @GetMapping
    public ResponseEntity<List<DtoEvaluacionItem>> getAllEvaluacionItem() {
        List<DtoEvaluacionItem> result = evaluacionItemService.findAll();
        if (result.isEmpty()) {
            throw new SearchEvaluacionItemNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Buscar item de evaluación por ID",
        description = "Recupera un item de evaluación específico mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item encontrado"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @GetMapping("/BuscarEvaluacionItem/{id}")
    public ResponseEntity<DtoEvaluacionItem> getEvaluacionItem(@PathVariable Integer id) {
        return ResponseEntity.ok(
            evaluacionItemService.findById(id)
                .orElseThrow(() -> new EvaluacionItemNotFoundException())
        );
    }

    @Operation(
        summary = "Crear nuevo item de evaluación",
        description = "Crea un nuevo item de evaluación con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Item creado correctamente",
            content = @Content(schema = @Schema(implementation = DtoEvaluacionItem.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos del item inválidos"
        )
    })
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

    @Operation(
        summary = "Crear múltiples items de evaluación",
        description = "Crea varios items de evaluación en una sola operación"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Items creados correctamente"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos de los items inválidos"
        )
    })
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

    @Operation(
        summary = "Modificar item de evaluación",
        description = "Actualiza los datos de un item de evaluación existente"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del item inválidos"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
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

    @Operation(
        summary = "Eliminar item de evaluación",
        description = "Elimina un item de evaluación existente mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Item eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @DeleteMapping("BorrarEvaluacionItem/{id}")
    public ResponseEntity<?> deleteEvaluacionItem(@PathVariable Integer id) {
        evaluacionItemService.findById(id)
            .orElseThrow(() -> new EvaluacionItemNotFoundException(id));
        evaluacionItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
