package com.haircutAPI.HaircutAPI.enity;

import com.haircutAPI.HaircutAPI.ENUM.CustomerTypes;

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
    String DoB;
    String email;
    String address;
    String phoneNumber;
    String startDate;
    String lastDayUsing;

    @Enumerated(EnumType.ORDINAL)
    CustomerTypes typeCustomer;


}
