package com.andaluciaskills.andaluciasckills.Controller;

import com.andaluciaskills.andaluciasckills.Dto.DtoParticipante;
import com.andaluciaskills.andaluciasckills.Error.ParticipanteBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.ParticipanteNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchParticipanteNoResultException;
import com.andaluciaskills.andaluciasckills.Service.ParticipanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/participantes")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@Tag(name = "Participantes", description = "API para la gestión de participantes")
public class ParticipanteController {
    
    private final ParticipanteService participanteService;

    @Operation(
        summary = "Listar todos los participantes",
        description = "Obtiene una lista de todos los participantes registrados en el sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de participantes recuperada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontraron participantes")
    })
    @GetMapping
    public ResponseEntity<List<DtoParticipante>> getAllParticipantes() {
        List<DtoParticipante> result = participanteService.findAll();
        if (result.isEmpty()) {
            throw new SearchParticipanteNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Buscar participante por ID",
        description = "Recupera un participante específico mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participante encontrado"),
        @ApiResponse(responseCode = "404", description = "Participante no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DtoParticipante> getParticipante(@PathVariable Integer id) {
        return participanteService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo participante",
        description = "Crea un nuevo participante con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Participante creado correctamente",
            content = @Content(schema = @Schema(implementation = DtoParticipante.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos del participante inválidos o ID ya existente"
        )
    })
    @PostMapping("/CrearParticipante")
    public ResponseEntity<DtoParticipante> createParticipante(@Valid @RequestBody DtoParticipante dto) {
        if (dto.getIdParticipante() != null) {
            throw new ParticipanteBadRequestException("No se puede crear un participante con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(participanteService.save(dto));
    }

    @Operation(
        summary = "Modificar participante",
        description = "Actualiza los datos de un participante existente"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participante actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del participante inválidos"),
        @ApiResponse(responseCode = "404", description = "Participante no encontrado")
    })
    @PutMapping("/ModificarParticipante/{id}")
    public ResponseEntity<DtoParticipante> updateParticipante(
            @PathVariable Integer id,
            @Valid @RequestBody DtoParticipante dto) {
    
        System.out.println("ID de la ruta: " + id);
        System.out.println("DTO recibido: " + dto);

        // Asegurarse de que el ID del DTO coincida con el de la ruta
        if (dto.getIdParticipante() != null && !dto.getIdParticipante().equals(id)) {
            throw new ParticipanteBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }

        // Establecer el ID del path en el DTO
        dto.setIdParticipante(id);

        DtoParticipante updated = participanteService.update(dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
        summary = "Eliminar participante",
        description = "Elimina un participante existente mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Participante eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Participante no encontrado")
    })
    @DeleteMapping("BorrarParticipante/{id}")
    public ResponseEntity<?> deleteParticipante(@PathVariable Integer id) {
        participanteService.findById(id)
            .orElseThrow(() -> new ParticipanteNotFoundException(id));
        participanteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener participantes por especialidad",
        description = "Recupera todos los participantes de una especialidad específica"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Participantes recuperados correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron participantes para la especialidad")
    })
    @GetMapping("/porEspecialidad/{especialidadId}")
    public ResponseEntity<List<DtoParticipante>> getParticipantesByEspecialidad(@PathVariable Integer especialidadId) {
        List<DtoParticipante> participantes = participanteService.findByEspecialidad(especialidadId);
        if (participantes.isEmpty()) {
            throw new SearchParticipanteNoResultException();
        }
        return ResponseEntity.ok(participantes);
    }
}