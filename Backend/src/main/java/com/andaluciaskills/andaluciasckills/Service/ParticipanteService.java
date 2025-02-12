package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoParticipante;
import com.andaluciaskills.andaluciasckills.Entity.Participante;
import com.andaluciaskills.andaluciasckills.Mapper.ParticipanteMapper;
import com.andaluciaskills.andaluciasckills.Repository.ParticipanteRepository;
import com.andaluciaskills.andaluciasckills.Service.base.ParticipanteBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipanteService implements ParticipanteBaseService {
    private final ParticipanteRepository participanteRepository;
    private final ParticipanteMapper participanteMapper;

    @Override
    public DtoParticipante save(DtoParticipante dto) {
        Participante participante = participanteMapper.toEntity(dto);
        Participante savedParticipante = participanteRepository.save(participante);
        return participanteMapper.toDto(savedParticipante);
    }

    @Override
    public Optional<DtoParticipante> findById(Integer id) {
        return participanteRepository.findById(id)
                .map(participanteMapper::toDto);
    }

    @Override
    public List<DtoParticipante> findAll() {
        return participanteMapper.toDtoList(participanteRepository.findAll());
    }

    @Override
    public DtoParticipante update(DtoParticipante dto) {
        Participante participante = participanteMapper.toEntity(dto);
        Participante updatedParticipante = participanteRepository.save(participante);
        return participanteMapper.toDto(updatedParticipante);
    }

    @Override
    public void delete(Integer id) {
        participanteRepository.deleteById(id);
    }
}
