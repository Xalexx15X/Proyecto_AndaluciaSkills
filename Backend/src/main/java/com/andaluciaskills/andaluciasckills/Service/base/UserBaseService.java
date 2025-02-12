package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Dto.DtoUser;

public interface UserBaseService {
    DtoUser save(DtoUser user);
    Optional<DtoUser> findById(Integer id);
    List<DtoUser> findAll();
    DtoUser update(DtoUser user);
    void delete(Integer id);
}