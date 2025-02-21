package com.andaluciaskills.andaluciasckills.Mapper;

import org.springframework.stereotype.Component;
import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluacion;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;

@Component 
public class EvaluacionMapper implements GenericMapper<Evaluacion, DtoEvaluacion> {

    @Override
    public DtoEvaluacion toDto(Evaluacion entity) {
        if (entity == null) return null;
        
        DtoEvaluacion dto = new DtoEvaluacion();
        dto.setIdEvaluacion(entity.getIdEvaluacion());
        dto.setNotaFinal(entity.getNotaFinal());
        dto.setParticipante_idParticipante(entity.getParticipante_idParticipante());
        dto.setUser_idUser(entity.getUser_idUser());
        dto.setPrueba_idPrueba(entity.getPrueba_idPrueba());
        return dto;
    }
    
    @Override
    public Evaluacion toEntity(DtoEvaluacion dto) {
        if (dto == null) return null;
        
        Evaluacion entity = new Evaluacion();
        entity.setIdEvaluacion(dto.getIdEvaluacion());
        entity.setNotaFinal(dto.getNotaFinal());
        entity.setParticipante_idParticipante(dto.getParticipante_idParticipante());
        entity.setUser_idUser(dto.getUser_idUser());
        entity.setPrueba_idPrueba(dto.getPrueba_idPrueba());
        return entity;
    }
}
