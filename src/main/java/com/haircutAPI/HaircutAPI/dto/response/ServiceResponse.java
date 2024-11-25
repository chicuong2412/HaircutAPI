package com.haircutAPI.HaircutAPI.dto.response;

import java.util.List;
import java.util.Set;

import com.haircutAPI.HaircutAPI.enity.Product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceResponse {

    String id;
    String name;
    String imgSrc;
    String description;
    long duration;
    double rate;
    double price;
    Set<Product> productsList;
}
