package org.seydaliev.jsonwebtokenauth.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

@Data
@RequiredArgsConstructor
public class UserPrincipal implements Principal {
    private final String id;
    private final String name;
    private final List<String> roles;
    @Override
    public String getName() {
        return name;
    }
}
