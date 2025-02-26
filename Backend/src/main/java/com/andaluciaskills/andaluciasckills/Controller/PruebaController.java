package com.andaluciaskills.andaluciasckills.Controller;


import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;
import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacion;
import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacionItem;
import com.andaluciaskills.andaluciasckills.Error.PruebaBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.PruebaNotFoundException;
import com.andaluciaskills.andaluciasckills.Service.EvaluacionItemService;
import com.andaluciaskills.andaluciasckills.Service.EvaluacionService;
import com.andaluciaskills.andaluciasckills.Service.ItemService;
import com.andaluciaskills.andaluciasckills.Service.PruebaService;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/pruebas")
@RequiredArgsConstructor
@Tag(name = "Pruebas", description = "API para la gestión de pruebas y sus evaluaciones")
public class PruebaController {
    private final PruebaService pruebaService;
    private final ItemService itemService;
    private final EvaluacionService evaluacionService;
    private final EvaluacionItemService evaluacionItemService;

    @Operation(
        summary = "Listar todas las pruebas",
        description = "Obtiene una lista de todas las pruebas registradas en el sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pruebas recuperada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontraron pruebas")
    })
    @GetMapping("/ListarPruebas")
    public ResponseEntity<List<DtoPrueba>> listarPruebas() {
        return ResponseEntity.ok(pruebaService.findAll());
    }

    @Operation(
        summary = "Listar pruebas por especialidad",
        description = "Obtiene todas las pruebas asociadas a una especialidad específica"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pruebas recuperadas con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/ListarPruebasPorEspecialidad/{especialidadId}")
    public ResponseEntity<List<DtoPrueba>> getPruebasByEspecialidad(@PathVariable Integer especialidadId) {
        try {
            List<DtoPrueba> pruebas = pruebaService.findByEspecialidadId(especialidadId);
            return ResponseEntity.ok(pruebas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
        summary = "Crear nueva prueba",
        description = "Crea una nueva prueba con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Prueba creada correctamente",
            content = @Content(schema = @Schema(implementation = DtoPrueba.class))
        ),
        @ApiResponse(responseCode = "500", description = "Error al crear la prueba")
    })
    @PostMapping("/CrearPrueba")
    public ResponseEntity<DtoPrueba> crearPrueba(@RequestBody DtoPrueba prueba) {
        try {
            DtoPrueba pruebaCreada = pruebaService.save(prueba);
            return ResponseEntity.status(HttpStatus.CREATED).body(pruebaCreada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
        summary = "Crear prueba con items",
        description = "Crea una nueva prueba junto con sus items asociados"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Prueba e items creados correctamente"),
        @ApiResponse(responseCode = "500", description = "Error al crear la prueba o los items")
    })
    @PostMapping("/CrearPruebaConItems")
    public ResponseEntity<DtoPrueba> crearPruebaConItems(@RequestBody Map<String, Object> request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DtoPrueba prueba = mapper.convertValue(request.get("prueba"), DtoPrueba.class);
            List<DtoItem> items = mapper.convertValue(
                request.get("items"), 
                new TypeReference<List<DtoItem>>() {}
            );
            
            DtoPrueba pruebaCreada = pruebaService.crearPruebaConItems(prueba, items);
            return ResponseEntity.status(HttpStatus.CREATED).body(pruebaCreada);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/CrearItems")
    public ResponseEntity<List<DtoItem>> crearItems(@RequestBody List<DtoItem> items) {
        try {
            System.out.println("Items recibidos: " + items); // Añadir log
            List<DtoItem> itemsCreados = itemService.saveAll(items);
            return ResponseEntity.status(HttpStatus.CREATED).body(itemsCreados);
        } catch (Exception e) {
            System.err.println("Error al crear items: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/BuscarPrueba/{id}")
    public ResponseEntity<DtoPrueba> buscarPrueba(@PathVariable Integer id) {
        return ResponseEntity.ok(pruebaService.findById(id)
                .orElseThrow(() -> new PruebaNotFoundException()));
    }

    @PutMapping("ModificarPrueba/{id}")
    public ResponseEntity<DtoPrueba> updatePrueba(
            @PathVariable Integer id, 
            @RequestBody DtoPrueba dto) {
        
        if (!id.equals(dto.getIdPrueba())) {
            throw new PruebaBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            pruebaService.findById(id)
                .map(e -> {
                    dto.setIdPrueba(id);
                    return pruebaService.save(dto);
                })
                .orElseThrow(() -> new PruebaNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarPrueba/{id}")
    public ResponseEntity<?> deletePrueba(@PathVariable Integer id) {
        pruebaService.findById(id)
            .orElseThrow(() -> new PruebaNotFoundException(id));
        pruebaService.delete(id);
        return ResponseEntity.noContent().build();
    } 

    @Operation(
        summary = "Actualizar valoraciones de evaluación",
        description = "Actualiza las valoraciones de los items de una evaluación y recalcula la nota final"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Valoraciones actualizadas correctamente"),
        @ApiResponse(responseCode = "500", description = "Error al actualizar las valoraciones")
    })
    @PostMapping("/evaluaciones/{evaluacionId}/valoraciones")
    public ResponseEntity<DtoEvaluacion> actualizarValoraciones(
            @PathVariable Integer evaluacionId,
            @RequestBody List<DtoEvaluacionItem> evaluacionItems) {
        try {
            System.out.println("Recibiendo items para evaluación: " + evaluacionId);
            
            // Guardamos los items
            for (DtoEvaluacionItem item : evaluacionItems) {
                evaluacionItemService.save(item);
            }
            
            // Actualizamos la nota final automáticamente
            DtoEvaluacion evaluacionActualizada = evaluacionService.actualizarNotaFinalAutomatica(evaluacionId);
            
            System.out.println("Evaluación actualizada: " + evaluacionActualizada);
            return ResponseEntity.ok(evaluacionActualizada);
        } catch (Exception e) {
            System.err.println("Error al actualizar valoraciones: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
        summary = "Crear evaluación",
        description = "Crea una nueva evaluación para una prueba y participante específicos"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Evaluación creada correctamente"),
        @ApiResponse(responseCode = "409", description = "Ya existe una evaluación para este participante en esta prueba"),
        @ApiResponse(responseCode = "400", description = "Error al crear la evaluación")
    })
    @PostMapping("/evaluacion")
    public ResponseEntity<DtoEvaluacion> crearEvaluacion(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Recibiendo request para crear evaluación: " + request);
            
            // Convertir los valores a Integer de forma segura
            Integer pruebaId = ((Number) request.get("pruebaId")).intValue();
            Integer participanteId = ((Number) request.get("participanteId")).intValue();
            Integer userId = ((Number) request.get("userId")).intValue();
            Double notaFinal = 0.0;

            // Verificar si ya existe una evaluación para esta prueba y participante
            boolean existeEvaluacion = pruebaService.existeEvaluacion(pruebaId, participanteId);
            if (existeEvaluacion) {
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT, 
                    "Ya existe una evaluación para este participante en esta prueba"
                );
            }

            DtoEvaluacion evaluacionCreada = evaluacionService.crearEvaluacion(pruebaId, participanteId, userId, notaFinal);
            
            System.out.println("Evaluación creada: " + evaluacionCreada);
            return ResponseEntity.status(HttpStatus.CREATED).body(evaluacionCreada);
        } catch (ResponseStatusException e) {
            throw e; // Re-lanzar excepciones de estado HTTP
        } catch (Exception e) {
            System.err.println("Error al crear evaluación: ");
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear la evaluación: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Verificar existencia de evaluación",
        description = "Comprueba si existe una evaluación para una prueba y participante específicos"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Verificación realizada correctamente"),
        @ApiResponse(responseCode = "500", description = "Error al realizar la verificación")
    })
    @GetMapping("/evaluacion/existe/{pruebaId}/{participanteId}")
    public ResponseEntity<Boolean> verificarEvaluacionExistente(
            @PathVariable Integer pruebaId,
            @PathVariable Integer participanteId) {
        try {
            boolean existe = pruebaService.existeEvaluacion(pruebaId, participanteId);
            return ResponseEntity.ok(existe);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
        summary = "Hace un pdf con la plantilla de evaluación",
        description = "Genera un pdf con la plantilla de evaluación para un prueba"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Plantilla generada correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/plantilla-evaluacion/{pruebaId}")
    public ResponseEntity<byte[]> generarPlantillaEvaluacion(@PathVariable Integer pruebaId) {
        try {
            byte[] plantilla = pruebaService.generarPlantillaEvaluacion(pruebaId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("plantilla-evaluacion.pdf").build());
            return new ResponseEntity<>(plantilla, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}