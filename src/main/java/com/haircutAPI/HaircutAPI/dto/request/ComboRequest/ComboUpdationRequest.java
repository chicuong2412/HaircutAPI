package com.haircutAPI.HaircutAPI.dto.request.ComboRequest;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComboUpdationRequest {

    @NotNull(message = "NOTNULL")
    String name;
    @NotNull(message = "NOTNULL")
    String description;
    @NotNull(message = "NOTNULL")
    long duration;
    @NotNull(message = "NOTNULL")
    double rate;
    @NotNull(message = "NOTNULL")
    double price;
}
