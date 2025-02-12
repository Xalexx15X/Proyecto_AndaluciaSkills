package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;
    
    private String role;
    private String username;
    private String password;
    private String nombre;
    private String apellidos;
    private String dni;

    @ManyToOne 
    @JoinColumn(name = "Especialidad_idEspecialidad")
    private Especialidad especialidad;
}