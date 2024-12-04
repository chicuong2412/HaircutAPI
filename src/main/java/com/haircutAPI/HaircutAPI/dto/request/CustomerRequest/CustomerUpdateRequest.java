package com.haircutAPI.HaircutAPI.dto.request.CustomerRequest;

import com.haircutAPI.HaircutAPI.ENUM.CustomerTypes;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerUpdateRequest {

    @Size(min = 8, message = "PASSWORD_LENGTH_INVALID")
    String password;
    String nameCustomer;
    double loyaltyPoint;
    LocalDate DoB;
    @Email(message = "EMAIL_INVALID")
    String email;
    String address;
    String phoneNumber;
    LocalDate startDate;
    LocalDate lastDayUsing;
    CustomerTypes typeCustomer;
    
}
