package com.andaluciaskills.andaluciasckills.Service.base;

import java.util.List;
import java.util.Optional;

import com.andaluciaskills.andaluciasckills.Entity.User;

public interface UserBaseService {
    User save(User user);
    Optional<User> findById(Integer id);
    List<User> findAll();
    User update(User user);
    void delete(Integer id);
}