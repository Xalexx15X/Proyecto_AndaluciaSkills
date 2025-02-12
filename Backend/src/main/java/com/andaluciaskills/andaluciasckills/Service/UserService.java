package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;

import com.andaluciaskills.andaluciasckills.Dto.DtoUser;
import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Mapper.UserMapper;
import com.andaluciaskills.andaluciasckills.Repository.UserRepository;
import com.andaluciaskills.andaluciasckills.Service.base.UserBaseService;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserBaseService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public DtoUser save(DtoUser dto) {
        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public Optional<DtoUser> findById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    public List<DtoUser> findAll() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    @Override
    public DtoUser update(DtoUser dto) {
        User user = userMapper.toEntity(dto);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}