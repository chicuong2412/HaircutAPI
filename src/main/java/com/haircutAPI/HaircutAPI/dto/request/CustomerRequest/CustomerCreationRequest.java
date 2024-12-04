package com.haircutAPI.HaircutAPI.dto.request.CustomerRequest;

import com.haircutAPI.HaircutAPI.ENUM.CustomerTypes;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerCreationRequest {

    @Size(min = 6, message = "USERNAME_LENGTH_INVALID")
    @NotNull(message = "NOTNULL")
    String username;

    @Size(min = 8, message = "PASSWORD_LENGTH_INVALID")
    @NotNull(message = "NOTNULL")
    String password;

    @NotNull(message = "NOTNULL")
    String nameCustomer;
    
    @NotNull(message = "NOTNULL")
    double loyaltyPoint;

    @NotNull(message = "NOTNULL")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate DoB;

    @Email(message = "EMAIL_INVALID")
    @NotNull(message = "NOTNULL")
    String email;

    @NotNull(message = "NOTNULL")
    String address;

    @NotNull(message = "NOTNULL")
    String phoneNumber;

    @NotNull(message = "NOTNULL")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate startDate;

    @NotNull(message = "NOTNULL")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate lastDayUsing;

    @NotNull(message = "NOTNULL")
    CustomerTypes typeCustomer;

}
