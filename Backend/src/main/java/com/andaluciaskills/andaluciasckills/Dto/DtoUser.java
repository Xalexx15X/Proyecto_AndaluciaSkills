package com.andaluciaskills.andaluciasckills.Dto;

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
}
