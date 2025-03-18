package com.haircutAPI.HaircutAPI.enity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class ServiceEntity {


    @Id
    @Column(name = "idService")
    String id;

    String name;
    String imgSrc = "../images/serviceDefault.png";

    @Column(columnDefinition = "TEXT")
    String description = "";
    long duration = 0;
    double rate = 0;
    double price = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Product> productsList;

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;
}
