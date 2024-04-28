package org.seydaliev.jsonwebtokenauth.repository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.seydaliev.jsonwebtokenauth.exception.RefreshTokenException;
import org.seydaliev.jsonwebtokenauth.model.RefreshToken;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
@Slf4j
public class RefreshTokenRepository {
    private static final String REFRESH_TOKEN_INDEX = "refreshTokenIndex";
    private final ReactiveValueOperations<String, RefreshToken> operationForValue;
    private final ReactiveHashOperations<String, String, String> operationForHash;


    public RefreshTokenRepository(ReactiveRedisTemplate<String, RefreshToken> redisTemplate) {
        this.operationForValue = redisTemplate.opsForValue();
        this.operationForHash = redisTemplate.opsForHash();
    }

    public Mono<Boolean> save(RefreshToken refreshToken, Duration expTime) {
        return operationForValue.set(refreshToken.getId(), refreshToken, expTime)
                .then(operationForHash.put(REFRESH_TOKEN_INDEX, refreshToken.getValue(), refreshToken.getId()));
    }

    public Mono<RefreshToken> getByValue(String refreshToken) {
        return operationForHash.get(REFRESH_TOKEN_INDEX, refreshToken)
                .flatMap(refreshTokenId -> operationForHash.remove(REFRESH_TOKEN_INDEX, refreshToken))
                .flatMap(cleanupCount -> {
                    log.info("Cleanup refreshToken hash count: {}", cleanupCount);

                    return operationForValue.get(refreshToken);
                })
                .switchIfEmpty(Mono.error(new RefreshTokenException("Refresh token not found: " + refreshToken)));
    }
}
