package com.haircutAPI.HaircutAPI.enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComboEntity {
    @Id
    @Column(name = "idCombo")
    String id;

    String name;
    String imgSrc;

    @Column(columnDefinition = "TEXT")
    String description;
    long duration;
    double rate;
    double price;
}
