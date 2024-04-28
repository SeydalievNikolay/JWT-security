package org.seydaliev.jsonwebtokenauth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenData {
    private String token;
    private String refreshToken;
}
