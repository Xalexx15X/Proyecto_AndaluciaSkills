package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoParticipante;
import com.andaluciaskills.andaluciasckills.Entity.Participante;
import com.andaluciaskills.andaluciasckills.Mapper.ParticipanteMapper;
import com.andaluciaskills.andaluciasckills.Repository.ParticipanteRepository;
import com.andaluciaskills.andaluciasckills.Service.base.ParticipanteBaseService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipanteService implements ParticipanteBaseService {
    @Autowired
    private ParticipanteRepository participanteRepository;
    @Autowired
    private ParticipanteMapper participanteMapper;

    @Override
    public List<DtoParticipante> findAll() {
        return participanteRepository.findAll().stream()
            .map(participanteMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<DtoParticipante> findById(Integer id) {
        return participanteRepository.findById(id)
            .map(participanteMapper::toDto);
    }

    @Override
    public DtoParticipante save(DtoParticipante dto) {
        Participante participante = participanteMapper.toEntity(dto);
        participante = participanteRepository.save(participante);
        return participanteMapper.toDto(participante);
    }

    @Override
    public DtoParticipante update(DtoParticipante dto) {
        Participante participante = participanteMapper.toEntity(dto);
        participante = participanteRepository.save(participante);
        return participanteMapper.toDto(participante);
    }

    @Override
    public void delete(Integer id) {
        participanteRepository.deleteById(id);
    }

    public List<DtoParticipante> findByEspecialidad(Integer especialidadId) {
        List<Participante> participantes = participanteRepository.findByEspecialidadIdEspecialidad(especialidadId);
        return participanteMapper.toDtoList(participantes);
    }
}