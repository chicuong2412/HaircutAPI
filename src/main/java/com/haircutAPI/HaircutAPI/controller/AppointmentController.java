package com.haircutAPI.HaircutAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
        return appointmentService.createAppointment(rq, SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping
    APIresponse<List<AppointmentResponse>> getAllAppointment() {
        return appointmentService.getAllAppointments();
    }

    @GetMapping("/{appointmentID}")
    public APIresponse<AppointmentResponse> getAppointment(@PathVariable String appointmentID) {
        return appointmentService.getAppointment(appointmentID, SecurityContextHolder.getContext().getAuthentication());
    }

    @PutMapping("/update")
    public APIresponse<AppointmentResponse> updateAppointment(@RequestBody @Valid AppointmentUpdationRequest rq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return appointmentService.updateAppointment(rq, authentication);
    }

    @DeleteMapping("/delete/{appointmentID}")
    public APIresponse<String> deleteAppointment(@PathVariable String appointmentID) {
        return appointmentService.deleteAppointment(appointmentID);
    }

    @GetMapping("/getByUsername/{username}")
    public APIresponse<List<AppointmentResponse>> getByUsername(@PathVariable String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return appointmentService.getAppointmentByCustomerUsername(username, authentication.getName());
    }

    @GetMapping("/getByIdWorker/{id}")
    public APIresponse<List<AppointmentResponse>> getAppointmetsByIdWorker(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return appointmentService.getAppointmentByIdWorker(authentication, id);
    }

    @GetMapping("/getByIdLocation/{id}")
    public APIresponse<List<AppointmentResponse>> getByIdLocation(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return appointmentService.getAppointmentByIdLocation(id, authentication);
    }

}
