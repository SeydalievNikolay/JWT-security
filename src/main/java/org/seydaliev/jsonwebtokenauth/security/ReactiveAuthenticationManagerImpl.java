package org.seydaliev.jsonwebtokenauth.security;

import jakarta.security.auth.message.AuthException;
import org.seydaliev.jsonwebtokenauth.model.UserPrincipal;
import org.seydaliev.jsonwebtokenauth.service.UserDetailsService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReactiveAuthenticationManagerImpl implements ReactiveAuthenticationManager {

    private final UserDetailsService userDetailsService;

    public ReactiveAuthenticationManagerImpl(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return userDetailsService.findByUsername(principal.getName())
                .filter(UserDetails::isEnabled)
                .switchIfEmpty(Mono.error(new AuthException("User disabled")))
                .map(userDetails -> authentication);
    }

}
