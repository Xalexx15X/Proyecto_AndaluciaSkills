package com.andaluciaskills.andaluciasckills.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que extrae y valida el token JWT de cada petición
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        // Extrae el token del header Authorization
        String token = extractToken(request);

        // Valida el token y configura la autenticación
        if(tokenProvider.isValidToken(token)) {
            // Obtiene el username del token
            String username = tokenProvider.getUsernameFromToken(token);
            // Carga los detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Crea un objeto Authentication con los detalles del usuario
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // Credenciales no necesarias aquí
                    userDetails.getAuthorities() // Roles/autoridades del usuario
            );

            // Establece la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // Continúa con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    // Extrae el token del header "Authorization: Bearer <token>"
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasLength(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Elimina "Bearer "
        }
        return null;
    }
}