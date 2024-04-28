package org.seydaliev.jsonwebtokenauth.service;

import org.seydaliev.jsonwebtokenauth.model.User;
import org.seydaliev.jsonwebtokenauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserService {
@Autowired
    private UserRepository userRepository;
@Autowired
    private PasswordEncoder passwordEncoder;

    public Mono<User> createUser(User user) {
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }
}
