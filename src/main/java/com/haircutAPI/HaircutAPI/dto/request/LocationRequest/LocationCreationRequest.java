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
    String imgSrc;

    @NotNull(message = "NOTNULL")
    String address;

    @NotNull(message = "NOTNULL")
    String city;

    @NotNull(message = "NOTNULL")
    String phoneNumber;

    @NotNull(message = "NOTNULL")
    String email;

    @NotNull(message = "NOTNULL")
    @DateTimeFormat(pattern = "hh:mm")
    LocalTime openHour;
}
