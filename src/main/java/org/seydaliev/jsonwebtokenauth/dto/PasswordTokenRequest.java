package org.seydaliev.jsonwebtokenauth.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordTokenRequest {
    private String username;
    private String password;
}
