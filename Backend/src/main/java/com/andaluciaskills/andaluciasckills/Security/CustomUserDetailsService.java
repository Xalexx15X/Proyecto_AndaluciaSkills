package com.andaluciaskills.andaluciasckills.Security;

import com.andaluciaskills.andaluciasckills.Entity.User;
import com.andaluciaskills.andaluciasckills.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("1. CustomUserDetailsService - Buscando usuario: " + username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("ERROR: Usuario no encontrado: " + username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        System.out.println("2. Usuario encontrado: " + user.getUsername());
        System.out.println("3. Rol del usuario: " + user.getRole());
        System.out.println("4. Password (hash): " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}