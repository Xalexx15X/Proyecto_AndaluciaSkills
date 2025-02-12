package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoPrueba;
import com.andaluciaskills.andaluciasckills.Entity.Prueba;
import com.andaluciaskills.andaluciasckills.Mapper.PruebaMapper;
import com.andaluciaskills.andaluciasckills.Repository.PruebaRepository;
import com.andaluciaskills.andaluciasckills.Service.base.PruebaBaseService;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PruebaService implements PruebaBaseService {
    private final PruebaRepository pruebaRepository;
    private final PruebaMapper pruebaMapper;

    @Override
    public DtoPrueba save(DtoPrueba dto) {
        Prueba prueba = pruebaMapper.toEntity(dto);
        Prueba savedPrueba = pruebaRepository.save(prueba);
        return pruebaMapper.toDto(savedPrueba);
    }

    @Override
    public Optional<DtoPrueba> findById(Integer id) {
        return pruebaRepository.findById(id)
                .map(pruebaMapper::toDto);
    }

    @Override
    public List<DtoPrueba> findAll() {
        return pruebaMapper.toDtoList(pruebaRepository.findAll());
    }

    @Override
    public DtoPrueba update(DtoPrueba dto) {
        Prueba prueba = pruebaMapper.toEntity(dto);
        Prueba updatedPrueba = pruebaRepository.save(prueba);
        return pruebaMapper.toDto(updatedPrueba);
    }

    @Override
    public void delete(Integer id) {
        pruebaRepository.deleteById(id);
    }

}