package com.andaluciaskills.andaluciasckills.Service;

import com.andaluciaskills.andaluciasckills.Entity.Prueba;
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

    @Override
    public Prueba save(Prueba prueba) {
        return pruebaRepository.save(prueba);
    }

    @Override
    public Optional<Prueba> findById(Integer id) {
        return pruebaRepository.findById(id);
    }

    @Override
    public List<Prueba> findAll() {
        return pruebaRepository.findAll();
    }

    @Override
    public Prueba update(Prueba prueba) {
        return pruebaRepository.save(prueba);
    }

    @Override
    public void delete(Integer id) {
        pruebaRepository.deleteById(id);
    }

}