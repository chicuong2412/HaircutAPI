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
public class Product {

    @Id
    @Column(name = "idProduct")
    String id;

    String name;
    long stockQuantity = 0;
    String imgSrc = "../images/productDefault.png";

    @Column(columnDefinition = "TEXT")
    String description = "";
    double price = 0;
    double rate = 0;

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;

}
