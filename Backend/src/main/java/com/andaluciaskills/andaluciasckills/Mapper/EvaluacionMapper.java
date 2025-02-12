package com.andaluciaskills.andaluciasckills.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.andaluciaskills.andaluciasckills.Dto.DtoEvaluancion;
import com.andaluciaskills.andaluciasckills.Entity.Evaluacion;
import com.andaluciaskills.andaluciasckills.Entity.Participante;
import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Entity.Prueba;

import com.andaluciaskills.andaluciasckills.Repository.ParticipanteRepository;
import com.andaluciaskills.andaluciasckills.Repository.PruebaRepository;
import com.andaluciaskills.andaluciasckills.Repository.UserRepository;

@Component 
public class EvaluacionMapper implements GenericMapper<Evaluacion, DtoEvaluancion> {

    @Autowired
    private ParticipanteRepository participanteRepository;
    private UserRepository userRepository;
    private PruebaRepository pruebaRepository;
    

    @Override
    public DtoEvaluancion toDto(Evaluacion entity) {
        if (entity == null) return null;
        
        DtoEvaluancion dto = new DtoEvaluancion();
        dto.setIdEvaluacion(entity.getIdEvaluacion());
        dto.setNotaFinal(entity.getNotaFinal());
        if (entity.getParticipante() != null) {
            dto.setParticipante_idParticipante(entity.getParticipante().getIdParticipante());
        }
        if (entity.getUser() != null) {
            dto.setUser_idUser(entity.getUser().getIdUser());
        }
        if (entity.getPrueba() != null) {
            dto.setPrueba_idPrueba(entity.getPrueba().getIdPrueba());
        }
        return dto;
    }
    
    @Override
    public Evaluacion toEntity(DtoEvaluancion dto) {
        if (dto == null) return null;
        
        Evaluacion entity = new Evaluacion();
        entity.setIdEvaluacion(dto.getIdEvaluacion());
        entity.setNotaFinal(dto.getNotaFinal());
        
        if (dto.getParticipante_idParticipante() != null) {
            Participante participante = participanteRepository
                .findById(dto.getParticipante_idParticipante())
                .orElse(null);
            entity.setParticipante(participante);
        }

        if (dto.getUser_idUser() != null) {
            User user = userRepository
                .findById(dto.getUser_idUser())
                .orElse(null);
            entity.setUser(user);
        }

        if (dto.getPrueba_idPrueba() != null) {
            Prueba prueba = pruebaRepository
                .findById(dto.getPrueba_idPrueba())
                .orElse(null);
            entity.setPrueba(prueba);
        }
    
        return entity;
    }
}
