package com.andaluciaskills.andaluciasckills.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andaluciaskills.andaluciasckills.Entity.Item;
import com.andaluciaskills.andaluciasckills.Error.ItemBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.ItemNotFoundException;
import com.andaluciaskills.andaluciasckills.Error.SearchItemNoResultException;
import com.andaluciaskills.andaluciasckills.Service.ItemService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/participantes")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<Item>> getAllEspecialidades() {
        List<Item> result = itemService.findAll();
        if (result.isEmpty()) {
            throw new SearchItemNoResultException();
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/BuscarItem/{id}")
    public ResponseEntity<Item> getItem(@RequestParam Integer id) {
        return ResponseEntity.ok(
            itemService.findById(id)
                .orElseThrow(() -> new SearchItemNoResultException())
        );
    }

    @PutMapping("ModificarItem/{id}")
    public ResponseEntity<Item> updateItem(
            @PathVariable Integer id, 
            @RequestBody Item item) {
        
        if (!id.equals(item.getIdItem())) {
            throw new ItemBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            itemService.findById(id)
                .map(e -> {
                    item.setIdItem(id);
                    return itemService.save(item);
                })
                .orElseThrow(() -> new ItemNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarItem/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable Integer id) {
        Item item = itemService.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

