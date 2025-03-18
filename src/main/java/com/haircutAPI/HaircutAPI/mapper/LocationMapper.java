package com.haircutAPI.HaircutAPI.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.haircutAPI.HaircutAPI.dto.request.LocationRequest.LocationCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.LocationRequest.LocationUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.LocationResponse;
import com.haircutAPI.HaircutAPI.enity.Location;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LocationMapper {

    LocationResponse toLocationResponse(Location location);

    Location toLocation(@MappingTarget Location location, LocationCreationRequest rq);

    List<LocationResponse> toLocationResponses(List<Location> locations);

    void updateLocation(@MappingTarget Location location, LocationUpdationRequest rq);
    
}
