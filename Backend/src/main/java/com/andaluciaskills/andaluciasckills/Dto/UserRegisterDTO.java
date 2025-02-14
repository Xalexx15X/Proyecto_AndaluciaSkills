package com.andaluciaskills.andaluciasckills.Dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String dni;
    private String role;
    private Integer especialidad_idEspecialidad;
}