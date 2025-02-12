package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Dto.DtoItem;

public interface ItemBaseService {
    DtoItem save(DtoItem item);
    Optional<DtoItem> findById(Integer id);
    List<DtoItem> findAll();
    DtoItem update(DtoItem item);
    void delete(Integer id);
}
