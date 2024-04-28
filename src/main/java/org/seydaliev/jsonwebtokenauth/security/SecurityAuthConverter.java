package org.seydaliev.jsonwebtokenauth.security;

import org.seydaliev.jsonwebtokenauth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityAuthConverter implements ServerAuthenticationConverter {

    private static final String BEARER_PREFIX = "Bearer ";
    @Autowired
    private TokenService tokenService;

    private Mono<String> extractBearerToken(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION))
                .flatMap(token -> {
                    if (token.startsWith(BEARER_PREFIX)) {
                        return Mono.just(token.substring(BEARER_PREFIX.length()));
                    }

                    return Mono.empty();
                });
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange)
                .flatMap(this::extractBearerToken)
                .flatMap(tokenService::toAuthentication);
    }
}
