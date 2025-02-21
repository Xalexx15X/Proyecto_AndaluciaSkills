package com.andaluciaskills.andaluciasckills.Controller;

import com.andaluciaskills.andaluciasckills.Dto.AuthResponseDTO;
import com.andaluciaskills.andaluciasckills.Dto.DtoUser;
import com.andaluciaskills.andaluciasckills.Dto.UserRegisterDTO;
import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Security.JwtTokenProvider;
import com.andaluciaskills.andaluciasckills.Service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;


@RestController // Indica que es un controlador REST
@RequestMapping("/api/auth") // Ruta base para todos los endpoints de este controlador
@RequiredArgsConstructor // Genera constructor con campos final automáticamente
public class AuthController {

    // Añade esta línea con las otras dependencias
    private final PasswordEncoder passwordEncoder;
    
    // Gestiona la autenticación de usuarios
    private final AuthenticationManager authenticationManager;
    // Genera y valida tokens JWT
    private final JwtTokenProvider jwtTokenProvider;
    // Accede a operaciones de usuario en base de datos
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody DtoUser loginDto) {
        try {             
            User user = userService.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
                
            boolean matches = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
            System.out.println("3. ¿Coinciden las contraseñas?: " + matches);
            
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );
            
            String token = jwtTokenProvider.generateToken(authentication);
                
            return ResponseEntity.ok(new AuthResponseDTO(token, user.getUsername(), user.getRole(), user.getEspecialidadId(), user.getApellidos(), user.getNombre()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthResponseDTO(null, null, null, null, null, null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            userService.registerUser(userRegisterDTO);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}