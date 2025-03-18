package com.haircutAPI.HaircutAPI.dto.response;

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
public class ComboResponse {

    String id;
    String name;
    String imgSrc;
    String description;
    long duration;
    double rate;
    double price;
    boolean deleted;
}
