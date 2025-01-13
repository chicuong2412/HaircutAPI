package com.haircutAPI.HaircutAPI.enity;

import com.haircutAPI.HaircutAPI.ENUM.CustomerTypes;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Customer {
    @Id
    String id;

    String username;
    String password;
    String nameCustomer;
    double loyaltyPoint;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate DoB;
    String email;
    String address;
    String phoneNumber;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate startDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate lastDayUsing;

    @Enumerated(EnumType.ORDINAL)
    CustomerTypes typeCustomer;

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;

}
