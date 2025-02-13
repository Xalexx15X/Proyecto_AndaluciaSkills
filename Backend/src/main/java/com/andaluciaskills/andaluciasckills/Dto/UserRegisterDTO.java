package com.andaluciaskills.andaluciasckills.Dto;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private String password2;
    private String nombre;
    private String apellidos;
    private String dni;
    private String role;
    private Integer especialidad_idEspecialidad;
}