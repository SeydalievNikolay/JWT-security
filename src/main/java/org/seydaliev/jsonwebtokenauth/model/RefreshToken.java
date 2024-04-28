package org.seydaliev.jsonwebtokenauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RefreshToken implements Serializable {
    private String id;
    private String userId;
    private String value;
}
