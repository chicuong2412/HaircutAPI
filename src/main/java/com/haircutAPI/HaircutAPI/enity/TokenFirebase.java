package com.haircutAPI.HaircutAPI.enity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenFirebase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String token;

    boolean isValid;

    @Override
    public boolean equals(Object obj) {
        TokenFirebase tokenFirebase = (TokenFirebase) obj;
        return tokenFirebase.token.equals(this.token);
    }
}
