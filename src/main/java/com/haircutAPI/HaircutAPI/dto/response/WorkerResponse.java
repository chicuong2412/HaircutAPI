package com.haircutAPI.HaircutAPI.dto.response;

import java.time.LocalDate;

import com.haircutAPI.HaircutAPI.ENUM.RoleEmployee;
import com.haircutAPI.HaircutAPI.enity.Location;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkerResponse {

    String id;
    String username;
    String nameWorker;
    String specialities;
    String imgSrc;
    double salary;
    double Rate;
    LocalDate DoB;
    String email;
    String address;
    String phoneNumber;
    Location location;
    boolean deleted;
    RoleEmployee idRole;

}
