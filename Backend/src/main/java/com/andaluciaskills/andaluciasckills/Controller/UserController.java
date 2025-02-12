package com.andaluciaskills.andaluciasckills.Controller;


import com.andaluciaskills.andaluciasckills.Dto.DtoUser;
import com.andaluciaskills.andaluciasckills.Error.SearchUserNoResultException;
import com.andaluciaskills.andaluciasckills.Error.UserBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.UserNotFoundException;
import com.andaluciaskills.andaluciasckills.Service.UserService;

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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

   @GetMapping
    public ResponseEntity<List<DtoUser>> getAllUsers() {
        List<DtoUser> result = userService.findAll();
        if (result.isEmpty()) { 
            throw new SearchUserNoResultException();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/BuscasUser/{id}")
    public ResponseEntity<DtoUser> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(
            userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException())
        );
    }

    @PostMapping("/CrearUser")
    public ResponseEntity<DtoUser> createUser(@RequestBody DtoUser dto) {
        if (dto.getIdUser() != null) {
            throw new UserBadRequestException("No se puede crear un participante con un ID ya existente");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.save(dto));
    }

    @PutMapping("ModificarUser/{id}")
    public ResponseEntity<DtoUser> updateUser(
            @PathVariable Integer id, 
            @RequestBody DtoUser dto) {
        
        if (!id.equals(dto.getIdUser())) {
            throw new UserBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            userService.findById(id)
                .map(e -> {
                    dto.setIdUser(id);
                    return userService.save(dto);
                })
                .orElseThrow(() -> new UserNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        userService.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        userService.delete(id);
        return ResponseEntity.noContent().build();
    } 
}
