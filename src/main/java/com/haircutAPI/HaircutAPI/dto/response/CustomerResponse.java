package com.haircutAPI.HaircutAPI.dto.response;

import java.time.LocalDate;

import com.haircutAPI.HaircutAPI.ENUM.CustomerTypes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {

    String id;
    String username;
    String nameCustomer;
    double loyaltyPoint;
    LocalDate DoB;
    String email;
    String address;
    String phoneNumber;
    LocalDate startDate;
    String lastDayUsing;
    CustomerTypes typeCustomer;
    boolean deleted;
    String imgSrc;

}
