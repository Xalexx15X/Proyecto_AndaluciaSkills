package com.andaluciaskills.andaluciasckills.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andaluciaskills.andaluciasckills.Dto.DtoItem;
import com.andaluciaskills.andaluciasckills.Error.ItemBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.ItemNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchItemNoResultException;
import com.andaluciaskills.andaluciasckills.Service.ItemService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Items", description = "API para la gestión de items de pruebas")
public class ItemController {
    private final ItemService itemService;

    @Operation(
        summary = "Listar todos los items",
        description = "Obtiene una lista de todos los items registrados en el sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de items recuperada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontraron items")
    })
    @GetMapping
    public ResponseEntity<List<DtoItem>> getAllItems() {
        List<DtoItem> result = itemService.findAll();
        if (result.isEmpty()) {
            throw new SearchItemNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @Operation(
        summary = "Buscar item por ID",
        description = "Recupera un item específico mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item encontrado"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @GetMapping("/BuscarItem/{id}")
    public ResponseEntity<DtoItem> getItem(@PathVariable Integer id) {
        return ResponseEntity.ok(
            itemService.findById(id)
                .orElseThrow(() -> new ItemNotFoundException())
        );
    }

    @Operation(
        summary = "Crear nuevo item",
        description = "Crea un nuevo item con los datos proporcionados"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201", 
            description = "Item creado correctamente",
            content = @Content(schema = @Schema(implementation = DtoItem.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Datos del item inválidos o ID ya existente"
        )
    })
    @PostMapping("/CrearItem")
    public ResponseEntity<DtoItem> createItem(@RequestBody DtoItem dto) {
        if (dto.getIdItem() != null) {
            throw new ItemBadRequestException("No se puede crear un item con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(itemService.save(dto));
    }

    @Operation(
        summary = "Modificar item",
        description = "Actualiza los datos de un item existente"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Item actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos del item inválidos"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @PutMapping("ModificarItem/{id}")
    public ResponseEntity<DtoItem> updateItem(@PathVariable Integer id, @RequestBody DtoItem dto) {
        
        if (!id.equals(dto.getIdItem())) {
            throw new ItemBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            itemService.findById(id)
                .map(e -> {
                    dto.setIdItem(id);
                    return itemService.save(dto);
                })
                .orElseThrow(() -> new ItemNotFoundException(id))
        );
    }

    @Operation(
        summary = "Eliminar item",
        description = "Elimina un item existente mediante su ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Item eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Item no encontrado")
    })
    @DeleteMapping("BorrarItem/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Integer id) {
        itemService.findById(id)
            .orElseThrow(() -> new ItemNotFoundException(id));
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obtener items por prueba",
        description = "Recupera todos los items asociados a una prueba específica"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Items recuperados correctamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/porPrueba/{pruebaId}")
    public ResponseEntity<List<DtoItem>> getItemsByPrueba(@PathVariable Integer pruebaId) {
        try {
            List<DtoItem> items = itemService.findByPruebaId(pruebaId);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

