package com.haircutAPI.HaircutAPI.enity;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location {

    @Id
    @Column(name = "idLocation")
    String id;

    String name;

    String imgSrc = "";

    @Column(columnDefinition = "TEXT")
    String address = "";

    String city = "";

    String phoneNumber = "";

    String email = "";

    LocalTime openHour = LocalTime.parse("08:30");

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;
    
}