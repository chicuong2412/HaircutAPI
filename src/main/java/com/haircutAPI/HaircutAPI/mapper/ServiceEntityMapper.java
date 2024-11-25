package com.haircutAPI.HaircutAPI.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.haircutAPI.HaircutAPI.dto.request.ServiceRequest.ServiceCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ServiceRequest.ServiceUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.ServiceResponse;
import com.haircutAPI.HaircutAPI.enity.ServiceEntity;


@Mapper(componentModel = "spring")
public interface ServiceEntityMapper {

    @Mapping(target = "productsList", ignore = true)
    ServiceEntity toServiceEntity(ServiceCreationRequest rq);

    @Mapping(target = "productsList", ignore = true)
    void updateServiceEntity(@MappingTarget ServiceEntity serviceEntity, ServiceUpdationRequest rq);

    ServiceResponse toServiceResponse(ServiceEntity serviceEntity);

    List<ServiceResponse> toServiceResponses(List<ServiceEntity> entities);
}
