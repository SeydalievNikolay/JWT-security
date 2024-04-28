package org.seydaliev.jsonwebtokenauth.service;

import org.seydaliev.jsonwebtokenauth.exception.RefreshTokenException;
import org.seydaliev.jsonwebtokenauth.model.RefreshToken;
import org.seydaliev.jsonwebtokenauth.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    public Mono<RefreshToken> save(String userId) {
        String refreshTokenValue = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken(id, userId, refreshTokenValue);
        return refreshTokenRepository.save(refreshToken, refreshTokenExpiration)
                .filter(isSuccess -> isSuccess)
                .flatMap(ignore -> Mono.just(refreshToken))
                .switchIfEmpty(Mono.error(new RefreshTokenException("Error on save refresh token for userId: " + userId)));
    }

    public Mono<RefreshToken> getByValue(String refreshToken) {
        return refreshTokenRepository.getByValue(refreshToken);
    }

}
