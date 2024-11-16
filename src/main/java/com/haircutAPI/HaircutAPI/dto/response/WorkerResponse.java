package com.haircutAPI.HaircutAPI.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkerResponse {

    String nameWorker;
    String specialities;
    double salary;
    double Rate;
    String DoB;
    String email;
    String address;
    String phoneNumber;
    int idLocation;

}
