package com.andaluciaskills.andaluciasckills.Controller;


import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;
import com.andaluciaskills.andaluciasckills.Error.PruebaBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.PruebaNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchPruebaNoResultException;
import com.andaluciaskills.andaluciasckills.Service.PruebaService;

import java.util.List;

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
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pruebas")
@RequiredArgsConstructor
public class PruebaController {
    private final PruebaService pruebaService;

   @GetMapping
    public ResponseEntity<List<DtoPrueba>> getAllPruebas() {
        List<DtoPrueba> result = pruebaService.findAll();
        if (result.isEmpty()) { 
            throw new SearchPruebaNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/BuscasPrueba/{id}")
    public ResponseEntity<DtoPrueba> getPrueba(@PathVariable Integer id) {
        return ResponseEntity.ok(
            pruebaService.findById(id)
                .orElseThrow(() -> new PruebaNotFoundException())
        );
    }

    @PostMapping("/CrearPrueba")
    public ResponseEntity<DtoPrueba> createPruebas(@RequestBody DtoPrueba dto) {
        if (dto.getIdPrueba() != null) {
            throw new PruebaBadRequestException("No se puede crear un participante con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(pruebaService.save(dto));
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
}