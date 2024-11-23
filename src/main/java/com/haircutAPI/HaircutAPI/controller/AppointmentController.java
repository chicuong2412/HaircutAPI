package com.haircutAPI.HaircutAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest.AppointmentUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.AppointmentResponse;
import com.haircutAPI.HaircutAPI.services.AppointmentService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    AppointmentService appointmentService;

    @PostMapping("/create")
    APIresponse<AppointmentResponse> postMethodName(@RequestBody @Valid AppointmentCreationRequest rq) {
        return appointmentService.createAppointment(rq);
    }

    @GetMapping
    APIresponse<List<AppointmentResponse>> getAllAppointment() {
        return appointmentService.getAllAppointments();
    }
    
    @GetMapping("/{appointmentID}")
    public APIresponse<AppointmentResponse> getAppointment(@PathVariable String appointmentID) {
        return appointmentService.getAppointment(appointmentID);
    }

    @PutMapping("/update")
    public APIresponse<String> updateAppointment(@RequestBody @Valid AppointmentUpdationRequest rq) {
        return appointmentService.updateAppointment(rq);
    }
    
    
    @DeleteMapping("/delete/{appointmentID}")
    public APIresponse<String> deleteAppointment(@PathVariable String appointmentID) {
        return appointmentService.deleteAppointment(appointmentID);
    }

    @GetMapping("/getByUsername/{username}")
    public APIresponse<List<AppointmentResponse>> getMethodName(@PathVariable String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return appointmentService.getAppointmentByCustomerUsername(username, authentication.getName());
    }
    
    
}
