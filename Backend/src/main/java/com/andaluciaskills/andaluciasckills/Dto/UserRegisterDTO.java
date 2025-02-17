package com.andaluciaskills.andaluciasckills.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserRegisterDTO {
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String dni;
    private String role;
    @JsonProperty("especialidad_id_especialidad")
    private Integer especialidadIdEspecialidad;
}
