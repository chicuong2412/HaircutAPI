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
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String header;
    
    String message;

}
