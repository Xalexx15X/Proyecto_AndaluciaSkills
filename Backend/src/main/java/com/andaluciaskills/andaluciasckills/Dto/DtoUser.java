package com.andaluciaskills.andaluciasckills.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DtoUser {
    private Integer idUser;
    private String role;
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String dni;
    @JsonProperty("Especialidad_idEspecialidad")
    private Integer especialidad_idEspecialidad;
    private String nombreEspecialidad;
}