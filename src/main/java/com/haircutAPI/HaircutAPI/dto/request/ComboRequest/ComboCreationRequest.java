package com.haircutAPI.HaircutAPI.dto.request.ComboRequest;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComboCreationRequest {

    @NotNull(message = "NOTNULL")
    String name;

    String file;

    String description;

    long duration;

    double rate;

    double price;
}
