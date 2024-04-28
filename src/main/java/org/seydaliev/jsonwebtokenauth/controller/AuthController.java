package org.seydaliev.jsonwebtokenauth.controller;

import org.seydaliev.jsonwebtokenauth.dto.CreateUserRequest;
import org.seydaliev.jsonwebtokenauth.dto.PasswordTokenRequest;
import org.seydaliev.jsonwebtokenauth.dto.RefreshTokenRequest;
import org.seydaliev.jsonwebtokenauth.model.*;
import org.seydaliev.jsonwebtokenauth.security.ReactiveAuthenticationManagerImpl;
import org.seydaliev.jsonwebtokenauth.service.JwtUtils;
import org.seydaliev.jsonwebtokenauth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private ReactiveAuthenticationManagerImpl authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private UserService userService;

    @PostMapping("/signing")
    public Mono<ResponseEntity<?>> authenticateUser(@RequestBody PasswordTokenRequest loginRequest,
                                                    @Autowired ReactiveAuthenticationManagerImpl reactiveAuthenticationManager) {
        return Mono.fromCallable(() -> {
            Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

            return reactiveAuthenticationManager.authenticate(authentication)
                    .map(auth -> {
                        SecurityContextHolder.getContext().setAuthentication(auth);

                        String jwt = jwtUtils.generateToken((UserDetails) auth.getPrincipal());

                        return ResponseEntity.ok(new RefreshTokenRequest(jwt));
                    })
                    .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed")));
        }).flatMap(Function.identity());
    }
    @PostMapping("/signup")
    public Mono<ResponseEntity<String>> registerUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(User.builder()
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .username(request.getUsername())
                        .roles(request.getRoles().stream().map(role -> Role.valueOf(role.toUpperCase())).collect(Collectors.toSet())).build())
                .map(user -> ResponseEntity.ok("User created"));
    }
}
