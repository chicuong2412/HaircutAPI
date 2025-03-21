package com.haircutAPI.HaircutAPI.enity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    String imgSrc = "../images/comboDefault.png";

    @Column(columnDefinition = "TEXT")
    String description = "";
    long duration = 0;
    double rate = 0;
    double price = 0;

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;
}
