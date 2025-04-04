package com.haircutAPI.HaircutAPI.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.haircutAPI.HaircutAPI.dto.request.ProductRequest.ProductCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ProductRequest.ProductUpdatioRequest;
import com.haircutAPI.HaircutAPI.dto.response.ProductResponse;
import com.haircutAPI.HaircutAPI.enity.Product;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductMapper {
    
    ProductResponse toProductResponse(Product product);

    Product toProduct(@MappingTarget Product product, ProductCreationRequest rq);

    void updateProduct(@MappingTarget Product product, ProductUpdatioRequest rq);

    List<ProductResponse> toProductResponses(List<Product> list);

}
