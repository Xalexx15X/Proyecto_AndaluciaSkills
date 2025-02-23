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

@RestController
@RequestMapping("/api/participantes")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ParticipanteController {
    
    private final ParticipanteService participanteService;

    @GetMapping
    public ResponseEntity<List<DtoParticipante>> getAllParticipantes() {
        List<DtoParticipante> result = participanteService.findAll();
        if (result.isEmpty()) {
            throw new SearchParticipanteNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DtoParticipante> getParticipante(@PathVariable Integer id) {
        return participanteService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/CrearParticipante")
    public ResponseEntity<DtoParticipante> createParticipante(@RequestBody DtoParticipante dto) {
        if (dto.getIdParticipante() != null) {
            throw new ParticipanteBadRequestException("No se puede crear un participante con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(participanteService.save(dto));
    }

    @PutMapping("/ModificarParticipante/{id}")
    public ResponseEntity<DtoParticipante> updateParticipante(
            @PathVariable Integer id,
            @RequestBody DtoParticipante dto) {
    
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

    @DeleteMapping("BorrarParticipante/{id}")
    public ResponseEntity<?> deleteParticipante(@PathVariable Integer id) {
        participanteService.findById(id)
            .orElseThrow(() -> new ParticipanteNotFoundException(id));
        participanteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/porEspecialidad/{especialidadId}")
    public ResponseEntity<List<DtoParticipante>> getParticipantesByEspecialidad(@PathVariable Integer especialidadId) {
        List<DtoParticipante> participantes = participanteService.findByEspecialidad(especialidadId);
        if (participantes.isEmpty()) {
            throw new SearchParticipanteNoResultException();
        }
        return ResponseEntity.ok(participantes);
    }
}