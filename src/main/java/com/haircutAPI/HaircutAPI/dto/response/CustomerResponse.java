package com.haircutAPI.HaircutAPI.dto.response;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResponse {

    String username;
    String nameCustomer;
    double loyaltyPoint;
    LocalDate DoB;
    String email;
    String address;
    String phoneNumber;
    LocalDate startDate;
    String lastDayUsing;

}
