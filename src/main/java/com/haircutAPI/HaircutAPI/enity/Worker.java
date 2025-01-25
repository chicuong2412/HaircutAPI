package com.haircutAPI.HaircutAPI.enity;

import java.time.LocalDate;

import com.haircutAPI.HaircutAPI.ENUM.RoleEmployee;

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
public class Worker {

    @Id
    String id;

    String username;
    String password;
    String nameWorker = "";
    String specialities = "";
    double salary = 0;
    double Rate = 0;
    LocalDate DoB = LocalDate.now();
    String email = "";
    String address = "";
    String phoneNumber = "";
    String idLocation;

    @Enumerated(EnumType.ORDINAL)
    RoleEmployee idRole = RoleEmployee.EMPLOYEE;
    LocalDate startDate = LocalDate.now();

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;

}
