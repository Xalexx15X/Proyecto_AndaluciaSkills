package com.andaluciaskills.andaluciasckills.Controller;


import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Error.SearchPruebaNoResultException;
import com.andaluciaskills.andaluciasckills.Error.SearchUserNoResultException;
import com.andaluciaskills.andaluciasckills.Error.UserBadRequestException;
import com.andaluciaskills.andaluciasckills.Error.UserNotFoundException;
import com.andaluciaskills.andaluciasckills.Service.UserService;

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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        List<User> result = userService.findAll();
        if (result.isEmpty()) {
            throw new SearchPruebaNoResultException();
        }
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/BuscarUser/{id}")
    public ResponseEntity<User> getUser(@RequestParam Integer id) { 
        return ResponseEntity.ok(
            userService.findById(id)
                .orElseThrow(() -> new SearchUserNoResultException())
        );
    }

    @PutMapping("ModificarUser/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Integer id, 
            @RequestBody User user) {
        
        if (!id.equals(user.getIdUser())) {
            throw new UserBadRequestException("El ID de la ruta no coincide con el ID del objeto");
        }
        
        return ResponseEntity.ok(
            userService.findById(id)
                .map(e -> {
                    user.setIdUser(id);
                    return userService.save(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id))
        );
    }

    @DeleteMapping("BorrarUser/{id}")   
    public ResponseEntity<User> deleteUser(@PathVariable Integer id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
