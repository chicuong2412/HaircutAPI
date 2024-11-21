package com.haircutAPI.HaircutAPI.dto.request.ComboRequest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComboCreationRequest {
    String name;
    String description;
    long duration;
    double rate;
    double price;
}
