package org.seydaliev.jsonwebtokenauth.controller;

import org.seydaliev.jsonwebtokenauth.dto.PasswordTokenRequest;
import org.seydaliev.jsonwebtokenauth.dto.RefreshTokenRequest;
import org.seydaliev.jsonwebtokenauth.dto.TokenResponse;
import org.seydaliev.jsonwebtokenauth.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/v1/public/token")
public class TokenController {
    @Autowired
    private SecurityService securityService;

    @PostMapping("/password")
    public Mono<ResponseEntity<TokenResponse>> password(@RequestBody PasswordTokenRequest passwordTokenRequest) {
        return securityService.processPasswordToken(passwordTokenRequest.getUsername(), passwordTokenRequest.getPassword())
                .map(tokenData -> ResponseEntity.ok(new TokenResponse(tokenData.getToken(), tokenData.getRefreshToken())));
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity<TokenResponse>> refresh(@RequestBody RefreshTokenRequest request) {
        return securityService.processRefreshToken(request.getRefreshToken())
                .map(tokenData -> ResponseEntity.ok(new TokenResponse(tokenData.getToken(), tokenData.getRefreshToken())));
    }
}
