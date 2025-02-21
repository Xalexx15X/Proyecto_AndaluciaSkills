package com.andaluciaskills.andaluciasckills.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String username;
    private String role;
    private Integer especialidadId;
    private String apellidos; // Cambiado a String
    private String nombre; // Cambiado a String
}