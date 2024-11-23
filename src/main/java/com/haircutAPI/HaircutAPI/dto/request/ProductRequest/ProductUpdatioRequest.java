package com.haircutAPI.HaircutAPI.dto.request.ProductRequest;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdatioRequest {

    @NotNull(message = "NOTNULL")
    String name;
    @NotNull(message = "NOTNULL")
    long stockQuantity;

    @NotNull(message = "NOTNULL")
    String description;
    @NotNull(message = "NOTNULL")
    double price;
    @NotNull(message = "NOTNULL")
    double rate;
}
