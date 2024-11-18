package com.haircutAPI.HaircutAPI.enity;

import java.util.List;

import jakarta.persistence.Column;
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
public class Service {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idService")
    String id;

    String name;
    String Description;
    long duration;
    double rate;
    double price;
    List<String> productsList;
}
