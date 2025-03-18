package com.haircutAPI.HaircutAPI.dto.request.LocationRequest;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationCreationRequest {

    @NotNull(message = "NOTNULL")
    String name;

    @NotNull(message = "NOTNULL")
    String address;

    String city;

    String phoneNumber;

    String email;

    @DateTimeFormat(pattern = "hh:mm")
    LocalTime openHour;

    String file;
}
