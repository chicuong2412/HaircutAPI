package com.haircutAPI.HaircutAPI.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.ServiceRequest.ServiceCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ServiceRequest.ServiceUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.ServiceResponse;
import com.haircutAPI.HaircutAPI.services.ServiceEntityService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/services")
public class ServiceEntityController {
    
    @Autowired
    ServiceEntityService serviceEntityService;

    @PostMapping("")
    public APIresponse<ServiceResponse> createService(@RequestBody @Valid ServiceCreationRequest rq) {
        System.out.println(rq.getDescription());
        return serviceEntityService.createService(rq);
    }

    @PutMapping("/{id}")
    public APIresponse<ServiceResponse> updateService(@PathVariable String id, @RequestBody @Valid ServiceUpdationRequest rq) {
        return serviceEntityService.updateService(rq, id);
    }


    @GetMapping("/getServiceByID/{id}")
    public APIresponse<ServiceResponse> getService(@PathVariable String id) {
        return serviceEntityService.getServiceEntity(id);
    }

    @GetMapping("/getAllServices")
    public APIresponse<List<ServiceResponse>> getAllServices() {
        return serviceEntityService.getAllServiceEntity();
    }

    @GetMapping("/getAllPublicServices")
    public APIresponse<List<ServiceResponse>> getAllPublicServices() {
        return serviceEntityService.getAllPublicServiceEntity();
    }
    
    @DeleteMapping("/delete/{idService}")
    APIresponse<String> deleteService(@PathVariable String idService) {
        serviceEntityService.deleteServiceEntity(idService);
        APIresponse<String> response = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());
        response.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());
        return response;
    }
    
    
}
