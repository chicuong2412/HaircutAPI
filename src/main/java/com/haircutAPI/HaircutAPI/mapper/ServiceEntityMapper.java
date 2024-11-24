package com.haircutAPI.HaircutAPI.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.haircutAPI.HaircutAPI.dto.request.ServiceRequest.ServiceCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ServiceRequest.ServiceUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.ServiceResponse;
import com.haircutAPI.HaircutAPI.enity.ServiceEntity;

@Mapper
public interface ServiceEntityMapper {

    ServiceEntity toServiceEntity(ServiceCreationRequest rq);

    void updateServiceEntity(@MappingTarget ServiceEntity serviceEntity, ServiceUpdationRequest rq);

    ServiceResponse toServiceResponse(ServiceEntity serviceEntity);

    List<ServiceResponse> toServiceResponses(List<ServiceEntity> entities);
}
