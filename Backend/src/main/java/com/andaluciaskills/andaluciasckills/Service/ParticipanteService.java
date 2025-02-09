package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Entity.Participante;
import com.andaluciaskills.andaluciasckills.Repository.ParticipanteRepository;
import com.andaluciaskills.andaluciasckills.Service.base.ParticipanteBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipanteService implements ParticipanteBaseService {
    private final ParticipanteRepository participanteRepository;

    @Override
    public Participante save(Participante participante) {
        return participanteRepository.save(participante);
    }

    @Override
    public Optional<Participante> findById(Integer id) {
        return participanteRepository.findById(id);
    }

    @Override
    public List<Participante> findAll() {
        return participanteRepository.findAll();
    }

    @Override
    public Participante update(Participante participante) {
        return participanteRepository.save(participante);
    }

    @Override
    public void delete(Integer id) {
        participanteRepository.deleteById(id);
    }
}
