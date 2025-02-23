package com.andaluciaskills.andaluciasckills.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String username;
    private String role;
    private String nombre;
    private String apellidos;
    private Integer especialidadId;
    private Integer idUser;
}