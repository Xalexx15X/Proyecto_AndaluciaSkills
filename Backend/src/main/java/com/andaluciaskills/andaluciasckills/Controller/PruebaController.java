package com.andaluciaskills.andaluciasckills.Controller;


import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;
import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Error.PruebaBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.PruebaNotFoundException;
import com.andaluciaskills.andaluciasckills.Service.FileStorageService;
import com.andaluciaskills.andaluciasckills.Service.PruebaService;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

@RestController
@RequestMapping("/api/pruebas")
@RequiredArgsConstructor
public class PruebaController {
    private final PruebaService pruebaService;
    private final FileStorageService fileStorageService;

    @GetMapping("/ListarPruebas")
    public ResponseEntity<List<DtoPrueba>> listarPruebas() {
        return ResponseEntity.ok(pruebaService.findAll());
    }

    @PostMapping("/CrearPrueba")
    public ResponseEntity<DtoPrueba> crearPrueba(@RequestBody DtoPrueba prueba) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pruebaService.save(prueba));
    }

    @PostMapping("/CrearPruebaConItems")
    public ResponseEntity<DtoPrueba> crearPruebaConItems(@RequestBody Map<String, Object> request) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            
            // Convertir el JSON a objetos
            DtoPrueba prueba = mapper.convertValue(request.get("prueba"), DtoPrueba.class);
            List<DtoItem> items = mapper.convertValue(
                request.get("items"), 
                new TypeReference<List<DtoItem>>() {}
            );
            
            // Llamar al servicio que maneja la transacción
            DtoPrueba pruebaCreada = pruebaService.crearPruebaConItems(prueba, items);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(pruebaCreada);
        } catch (Exception e) {
            // Mejorar el manejo de errores
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }

    @PostMapping("/CrearPruebaConFile")
    public ResponseEntity<?> crearPruebaConFile(
        @RequestParam("file") MultipartFile file,
        @RequestParam("prueba") String pruebaJson
    ) {
        try {
            // Guardar el archivo
            String fileName = fileStorageService.storeFile(file);
            String fileUrl = "/uploads/" + fileName;

            // Convertir JSON a objeto
            ObjectMapper mapper = new ObjectMapper();
            DtoPrueba dtoPrueba = mapper.readValue(pruebaJson, DtoPrueba.class);
            
            // Establecer la URL del archivo como enunciado
            dtoPrueba.setEnunciado(fileUrl);

            // Crear la prueba usando el método save() en lugar de crearPrueba()
            DtoPrueba pruebaCreada = pruebaService.save(dtoPrueba);
            return ResponseEntity.status(HttpStatus.CREATED).body(pruebaCreada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
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

}