package com.haircutAPI.HaircutAPI.dto.response;

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
    String DoB;
    String email;
    String address;
    String phoneNumber;
    String startDate;
    String lastDayUsing;

}
