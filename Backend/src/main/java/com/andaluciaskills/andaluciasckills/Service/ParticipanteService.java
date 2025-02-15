package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoParticipante;
import com.andaluciaskills.andaluciasckills.Entity.Participante;
import com.andaluciaskills.andaluciasckills.Mapper.ParticipanteMapper;
import com.andaluciaskills.andaluciasckills.Repository.ParticipanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipanteService {
    private final ParticipanteRepository participanteRepository;
    private final ParticipanteMapper participanteMapper;

    public List<DtoParticipante> findAll() {
        return participanteRepository.findAll().stream()
            .map(participanteMapper::toDto)
            .collect(Collectors.toList());
    }

    public Optional<DtoParticipante> findById(Integer id) {
        return participanteRepository.findById(id)
            .map(participanteMapper::toDto);
    }

    public DtoParticipante save(DtoParticipante dto) {
        Participante participante = participanteMapper.toEntity(dto);
        participante = participanteRepository.save(participante);
        return participanteMapper.toDto(participante);
    }

    public void delete(Integer id) {
        participanteRepository.deleteById(id);
    }
}