package com.andaluciaskills.andaluciasckills.Service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.andaluciaskills.andaluciasckills.Dto.DtoUser;
import com.andaluciaskills.andaluciasckills.Dto.UserRegisterDTO;
import com.andaluciaskills.andaluciasckills.Entity.Especialidad;
import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Mapper.UserMapper;
import com.andaluciaskills.andaluciasckills.Repository.EspecialidadRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final EspecialidadRepository especialidadRepository;

    @Override
    public DtoUser save(DtoUser dto) {
        if (dto.getPassword() != null) {
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
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

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerUser(UserRegisterDTO registerDTO) {
        // Verificar si el usuario ya existe
        if (userRepository.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        // Buscar la especialidad
        Especialidad especialidad = especialidadRepository
            .findById(registerDTO.getEspecialidadIdEspecialidad())
            .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        // Crear nuevo usuario
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNombre(registerDTO.getNombre());
        user.setApellidos(registerDTO.getApellidos());
        user.setDni(registerDTO.getDni());
        user.setRole(registerDTO.getRole());
        user.setEspecialidad(especialidad); // Aquí establecemos la relación correctamente

        return userRepository.save(user);
    }

    // Métodos para manejar las operaciones de expertos
    public List<DtoUser> findAllExpertos() {
        return userMapper.toDtoList(userRepository.findByRole("ROLE_EXPERTO"));
    }

    public Optional<DtoUser> findExpertoById(Integer id) {
        return userRepository.findByIdUserAndRole(id, "ROLE_EXPERTO")
                .map(userMapper::toDto);
    }
}
