
package com.andaluciaskills.andaluciasckills.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.andaluciaskills.andaluciasckills.Entity.Participante;
import com.andaluciaskills.andaluciasckills.Error.ItemBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.ItemNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchParticipanteNoResultException;
import com.andaluciaskills.andaluciasckills.Service.ParticipanteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/participantes")
@RequiredArgsConstructor
public class ParticipanteController {
    private final ParticipanteService participanteService;

    @GetMapping
    public ResponseEntity<List<Participante>> getAllParticipantes() {
        List<Participante> result = participanteService.findAll();
        if (result.isEmpty()) {
            throw new SearchParticipanteNoResultException();
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/BuscarParticipante/{id}")
    public ResponseEntity<Participante> getParticipante(@RequestParam Integer id) {
        return ResponseEntity.ok(
            participanteService.findById(id)
                .orElseThrow(() -> new SearchParticipanteNoResultException())
        );
    }

    @PutMapping("ModificarParticipante/{id}")
    public ResponseEntity<Participante> updateParticipante(
            @PathVariable Integer id, 
            @RequestBody Participante participante) {
        
        if (!id.equals(participante.getIdParticipante())) {
            throw new ItemBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            participanteService.findById(id)
                .map(e -> {
                    participante.setIdParticipante(id);
                    return participanteService.save(participante);
                })
                .orElseThrow(() -> new ItemNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarParticipante/{id}")
    public ResponseEntity<Participante> deleteParticipante(@PathVariable Integer id) {
        Participante participante = participanteService.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        participanteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
