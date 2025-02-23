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


@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<DtoItem>> getAllItems() {
        List<DtoItem> result = itemService.findAll();
        if (result.isEmpty()) {
            throw new SearchItemNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/BuscarItem/{id}")
    public ResponseEntity<DtoItem> getItem(@PathVariable Integer id) {
        return ResponseEntity.ok(
            itemService.findById(id)
                .orElseThrow(() -> new ItemNotFoundException())
        );
    }

    @PostMapping("/CrearItem")
    public ResponseEntity<DtoItem> createItem(@RequestBody DtoItem dto) {
        if (dto.getIdItem() != null) {
            throw new ItemBadRequestException("No se puede crear un item con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(itemService.save(dto));
    }

    @PutMapping("ModificarItem/{id}")
    public ResponseEntity<DtoItem> updateItem(
            @PathVariable Integer id, 
            @RequestBody DtoItem dto) {
        
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

    @DeleteMapping("BorrarItem/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Integer id) {
        itemService.findById(id)
            .orElseThrow(() -> new ItemNotFoundException(id));
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }

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

