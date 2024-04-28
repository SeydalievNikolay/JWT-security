package org.seydaliev.jsonwebtokenauth.service;

import org.seydaliev.jsonwebtokenauth.dto.UserDetailsDto;
import org.seydaliev.jsonwebtokenauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public Mono<UserDetails> findByUsername(String username){
        return userRepository.findByUsername(username).map(UserDetailsDto::new);
    }
}
