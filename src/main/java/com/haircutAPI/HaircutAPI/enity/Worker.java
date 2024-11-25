package com.haircutAPI.HaircutAPI.enity;

import com.haircutAPI.HaircutAPI.ENUM.RoleEmployee;

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
    String nameWorker;
    String specialities;
    double salary;
    double Rate;
    String DoB;
    String email;
    String address;
    String phoneNumber;
    String idLocation;

    @Enumerated(EnumType.ORDINAL)
    RoleEmployee idRole;
    String startDate;

    

}
