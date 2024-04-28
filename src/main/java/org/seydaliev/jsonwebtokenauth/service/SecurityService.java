package org.seydaliev.jsonwebtokenauth.service;

import jakarta.security.auth.message.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.seydaliev.jsonwebtokenauth.dto.TokenData;
import org.seydaliev.jsonwebtokenauth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SecurityService {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public Mono<TokenData> processPasswordToken(String username, String password) {
        return userService.findByUsername(username)
                .flatMap(user -> {
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new AuthException("Exception trying to check password for user: " + username));
                    }

                    return createTokenData(user);
                });
    }

    public Mono<TokenData> processRefreshToken(String refreshTokenValue) {
        return refreshTokenService.getByValue(refreshTokenValue)
                .flatMap(refreshToken -> userService.findById(refreshToken.getUserId()))
                .flatMap(this::createTokenData);
    }

    private Mono<TokenData> createTokenData(User user) {
        String token = tokenService.generateToken(
                user.getUsername(),
                user.getId(),
                user.getRoles().stream().map(Enum::name).toList());

        return refreshTokenService.save(user.getId())
                .flatMap(refreshToken -> Mono.just(new TokenData(token, refreshToken.getValue())));
    }
}
