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

    String name;

    String file;

    long stockQuantity;

    String description;

    double price;

    double rate;
}
