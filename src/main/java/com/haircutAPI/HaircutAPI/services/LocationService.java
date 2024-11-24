package com.haircutAPI.HaircutAPI.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.LocationRequest.LocationCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.LocationRequest.LocationUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.LocationResponse;
import com.haircutAPI.HaircutAPI.enity.Location;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.LocationMapper;
import com.haircutAPI.HaircutAPI.repositories.LocationRepository;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LocationMapper locationMapper;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<LocationResponse> createLocation(LocationCreationRequest rq) {
        APIresponse<LocationResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        Location location = locationMapper.toLocation(rq);

        locationRepository.save(location);

        rp.setResult(locationMapper.toLocationResponse(location));

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<LocationResponse> updateLocation(LocationUpdationRequest rq, String idLocation) {
        Location location = locationRepository.findById(idLocation)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        APIresponse<LocationResponse> rp = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());

        locationMapper.updateLocation(location, rq);

        locationRepository.save(location);

        rp.setResult(locationMapper.toLocationResponse(location));

        return rp;
    }

    public APIresponse<LocationResponse> getLocation(String idLocation) {
        Location location = locationRepository.findById(idLocation)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        APIresponse<LocationResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        rp.setResult(locationMapper.toLocationResponse(location));

        return rp;
    }

    public APIresponse<List<LocationResponse>> getLocations() {

        APIresponse<List<LocationResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        rp.setResult(locationMapper.toLocationResponses(locationRepository.findAll()));

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<String> deleteLocation(String idLocation) {

        if (!locationRepository.existsById(idLocation))
            throw new AppException(ErrorCode.ID_NOT_FOUND);

        APIresponse<String> rp = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());

        rp.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());

        locationRepository.deleteById(idLocation);

        return rp;
    }
}
