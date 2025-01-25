package com.haircutAPI.HaircutAPI.enity;

import com.haircutAPI.HaircutAPI.ENUM.CustomerTypes;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;
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
    @ColumnDefault(value = "none")
    String nameCustomer = "";

    @ColumnDefault(value = "0")
    double loyaltyPoint = 0;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate DoB = LocalDate.now();
    String email;
    @ColumnDefault(value = "none")
    String address = "";
    @ColumnDefault(value = "none")
    String phoneNumber = "";
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate startDate = LocalDate.now();
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate lastDayUsing = LocalDate.now();

    @ColumnDefault(value = "'0'")
    @Enumerated(EnumType.ORDINAL)
    CustomerTypes typeCustomer = CustomerTypes.None;

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;

}
