package org.seydaliev.jsonwebtokenauth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


public enum Role {
    USER,
    ADMIN
}
