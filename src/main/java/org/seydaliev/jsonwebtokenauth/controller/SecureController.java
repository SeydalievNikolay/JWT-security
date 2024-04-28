package org.seydaliev.jsonwebtokenauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/secure")
public class SecureController {

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Mono<String> adminAccess() {
        return Mono.just("Admin access");
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public Mono<String> userAccess() {
        return Mono.just("User access");
    }
}
