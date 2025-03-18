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

    String name;

    String file;

    String description;

    long duration;

    double rate;

    double price;
    List<String> productsList;
}