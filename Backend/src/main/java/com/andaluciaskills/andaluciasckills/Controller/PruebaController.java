package com.andaluciaskills.andaluciasckills.Controller;


import com.andaluciaskills.andaluciasckills.Entity.Participante;
import com.andaluciaskills.andaluciasckills.Entity.Prueba;
import com.andaluciaskills.andaluciasckills.Error.ItemBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.ItemNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.PruebaBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.PruebaNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchParticipanteNoResultException;
import com.andaluciaskills.andaluciasckills.Error.SearchPruebaNoResultException;
import com.andaluciaskills.andaluciasckills.Service.PruebaService;

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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pruebas")
@RequiredArgsConstructor
public class PruebaController {
    private final PruebaService pruebaService;

    @GetMapping
    public ResponseEntity<List<Prueba>> getAllPruebas() {
        List<Prueba> result = pruebaService.findAll();
        if (result.isEmpty()) {
            throw new SearchPruebaNoResultException();
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/BuscarPrueba/{id}")
    public ResponseEntity<Prueba> getPrueba(@RequestParam Integer id) { 
        return ResponseEntity.ok(
            pruebaService.findById(id)
                .orElseThrow(() -> new SearchPruebaNoResultException())
        );
    }

    @PutMapping("ModificarPrueba/{id}")
    public ResponseEntity<Prueba> updatePrueba(
            @PathVariable Integer id, 
            @RequestBody Prueba prueba) {
        
        if (!id.equals(prueba.getIdPrueba())) {
            throw new PruebaBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            pruebaService.findById(id)
                .map(e -> {
                    prueba.setIdPrueba(id);
                    return pruebaService.save(prueba);
                })
                .orElseThrow(() -> new PruebaNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarPrueba/{id}")
    public ResponseEntity<Prueba> deletePrueba(@PathVariable Integer id) {
        Prueba prueba = pruebaService.findById(id)
                .orElseThrow(() -> new PruebaNotFoundException(id));
        pruebaService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}