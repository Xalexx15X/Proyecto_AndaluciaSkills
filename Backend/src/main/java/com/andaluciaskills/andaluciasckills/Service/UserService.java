package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Repository.UserRepository;
import com.andaluciaskills.andaluciasckills.Service.base.UserBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserBaseService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}