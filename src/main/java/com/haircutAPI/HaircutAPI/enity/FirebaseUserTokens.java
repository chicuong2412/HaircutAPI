package com.haircutAPI.HaircutAPI.enity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FirebaseUserTokens {

    @Id
    String userID;
    
    @ManyToMany
    Set<TokenFirebase> token;
}
