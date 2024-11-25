package com.haircutAPI.HaircutAPI.enity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idService")
    String id;

    String name;
    String imgSrc;

    @Column(columnDefinition = "TEXT")
    String description;
    long duration;
    double rate;
    double price;

    @ManyToMany
    Set<Product> productsList;
}
