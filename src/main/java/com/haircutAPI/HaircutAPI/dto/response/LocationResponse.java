package com.haircutAPI.HaircutAPI.dto.response;

import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationResponse {
    
    String id;

    String name;

    String imgSrc;

    String address;

    String city;

    String phoneNumber;

    String email;

    LocalTime openHour;
    
}
