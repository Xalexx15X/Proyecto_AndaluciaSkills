package com.andaluciaskills.andaluciasckills.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Entity
@Data
@Table(name = "especialidades")
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEspecialidad;
    private String nombre;
    private String codigo;

    @ToString.Exclude
    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL)
    private List<Participante> participantes;

    @ToString.Exclude
    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL)
    private List<User> users;

    @ToString.Exclude
    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL)
    private List<Prueba> pruebas;
}