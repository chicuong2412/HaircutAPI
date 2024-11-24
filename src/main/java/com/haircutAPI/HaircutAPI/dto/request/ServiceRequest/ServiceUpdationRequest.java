package com.haircutAPI.HaircutAPI.dto.request.ServiceRequest;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceUpdationRequest {

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
    List<String> productsList;
}