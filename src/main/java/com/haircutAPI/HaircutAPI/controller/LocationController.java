package com.haircutAPI.HaircutAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.dto.request.LocationRequest.LocationCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.LocationRequest.LocationUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.LocationResponse;
import com.haircutAPI.HaircutAPI.services.LocationService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    LocationService locationService;

    @PostMapping("/create")
    public APIresponse<LocationResponse> createLocation(@RequestBody @Valid LocationCreationRequest rq) {
        return locationService.createLocation(rq);
    }

    @PutMapping("/update/{id}")
    public APIresponse<LocationResponse> updateLocation(@PathVariable String id, @RequestBody LocationUpdationRequest rq) {
        return locationService.updateLocation(rq, id);
    }

    @GetMapping("/getLocation/{id}")
    public APIresponse<LocationResponse> getLocation(@PathVariable String id) {
        return locationService.getLocation(id);
    }

    @GetMapping("/getLocations")
    public APIresponse<List<LocationResponse>> getAllLocations() {
        return locationService.getLocations();
    }

    @GetMapping("/getPublicLocations")
    public APIresponse<List<LocationResponse>> getAllPublicLocations() {
        return locationService.getPublicLocations();
    }

    @DeleteMapping("/delete/{id}")
    public APIresponse<String> deleteLocation(@PathVariable String id) {
        return locationService.deleteLocation(id);
    }

}
