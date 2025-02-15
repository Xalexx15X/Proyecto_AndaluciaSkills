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

    @GetMapping("/BuscasParticipante/{id}")
    public ResponseEntity<DtoParticipante> getParticipante(@PathVariable Integer id) {
        return ResponseEntity.ok(
            participanteService.findById(id)
                .orElseThrow(() -> new ParticipanteNotFoundException())
        );
    }

    @PostMapping("/CrearParticipante")
    public ResponseEntity<DtoParticipante> createParticipante(@RequestBody DtoParticipante dto) {
        if (dto.getIdParticipante() != null) {
            throw new ParticipanteBadRequestException("No se puede crear un participante con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(participanteService.save(dto));
    }

    @PutMapping("ModificarParticipante/{id}")
    public ResponseEntity<DtoParticipante> updateParticipante(
            @PathVariable Integer id,
            @RequestBody DtoParticipante dto) {

        if (!id.equals(dto.getIdParticipante())) {
            throw new ParticipanteBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }

        return ResponseEntity.ok(
            participanteService.findById(id)
                .map(e -> {
                    dto.setIdParticipante(id);
                    return participanteService.save(dto);
                })
                .orElseThrow(() -> new ParticipanteNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarParticipante/{id}")
    public ResponseEntity<?> deleteParticipante(@PathVariable Integer id) {
        participanteService.findById(id)
            .orElseThrow(() -> new ParticipanteNotFoundException(id));
        participanteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}