package com.andaluciaskills.andaluciasckills.Controller;

import com.andaluciaskills.andaluciasckills.Dto.AuthResponseDTO;
import com.andaluciaskills.andaluciasckills.Dto.DtoUser;
import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Security.JwtTokenProvider;
import com.andaluciaskills.andaluciasckills.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController // Indica que es un controlador REST
@RequestMapping("/api/auth") // Ruta base para todos los endpoints de este controlador
@RequiredArgsConstructor // Genera constructor con campos final automáticamente
public class AuthController {

    // Gestiona la autenticación de usuarios
    private final AuthenticationManager authenticationManager;
    // Genera y valida tokens JWT
    private final JwtTokenProvider jwtTokenProvider;
    // Accede a operaciones de usuario en base de datos
    private final UserService userService;

    @PostMapping("/login") // Endpoint para login: POST /api/auth/login
    public ResponseEntity<AuthResponseDTO> login(@RequestBody DtoUser loginDto) {
        // Intenta autenticar al usuario con las credenciales proporcionadas
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        // Si la autenticación es exitosa, genera un token JWT
        String token = jwtTokenProvider.generateToken(authentication);
        
        // Busca información adicional del usuario en la base de datos
        User user = userService.findByUsername(loginDto.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Devuelve el token y datos del usuario
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getUsername(), user.getRole()));
    }
}