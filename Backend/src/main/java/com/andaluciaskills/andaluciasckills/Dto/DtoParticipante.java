package com.andaluciaskills.andaluciasckills.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DtoParticipante {
    private Integer idParticipante;
    private String nombre;
    private String apellidos;
    private String centro;
    @JsonProperty("especialidad_id_especialidad")
    private Integer especialidadIdEspecialidad;
    private String nombreEspecialidad; 
}