package com.haircutAPI.HaircutAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerUpdateRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.CustomerResponse;
import com.haircutAPI.HaircutAPI.services.CustomerService;

import jakarta.validation.Valid;
// import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    APIresponse<CustomerResponse> createCustomer(@RequestBody @Valid CustomerCreationRequest request) {
        APIresponse<CustomerResponse> reponse = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());
        reponse.setResult(customerService.createCustomer(request));
        return reponse;
    }

    @GetMapping
    APIresponse<List<CustomerResponse>> getCustomers() {
        APIresponse<List<CustomerResponse>> reponse = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(customerService.getAllCustomers(""));
        return reponse;
    }

    @GetMapping("/getMyInfo")
    public APIresponse<CustomerResponse> getMyInfo() {
        return customerService.getMyInfo(SecurityContextHolder.getContext().getAuthentication());
    }

    @GetMapping("/{customerID}")
    APIresponse<CustomerResponse> getCustomer(@PathVariable String customerID) {
        APIresponse<CustomerResponse> reponse = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(customerService.getCustomerbyID(customerID));
        return reponse;
    }

    @PutMapping("/{customerID}")
    APIresponse<CustomerResponse> updateCustomerInfo(@PathVariable String customerID, @RequestBody @Valid CustomerUpdateRequest rq) {
        APIresponse<CustomerResponse> reponse = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(customerService.updateCustomer(customerID, rq));
        return reponse;
    }

    @DeleteMapping("/delete/{customerID}")
    APIresponse<String> deleteCustomer(@PathVariable String customerID) {
        customerService.deleteCustomer(customerID);
        APIresponse<String> response = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());
        response.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());
        return response;
    }
}
