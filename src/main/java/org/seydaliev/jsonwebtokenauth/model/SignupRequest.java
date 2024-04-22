package org.seydaliev.jsonwebtokenauth.model;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
}
