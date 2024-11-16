package com.haircutAPI.HaircutAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.services.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    APIresponse<CustomerResponse> createWorker(@RequestBody @Valid CustomerCreationRequest request) {
        APIresponse<CustomerResponse> reponse = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());
        reponse.setResult(customerService.createCustomer(request));
        return reponse;
    }

    @GetMapping
    APIresponse<List<CustomerResponse>> getWorkers() {
        APIresponse<List<CustomerResponse>> reponse = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(customerService.getAllCustomers());
        return reponse;
    }

    @GetMapping("/searchName/{name}")
    APIresponse<List<CustomerResponse>> getListSearchByName(@PathVariable String name) {
        APIresponse<List<CustomerResponse>> reponse = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(customerService.searchByName(name));
        return reponse;
    }

    @GetMapping("/{customerID}")
    APIresponse<CustomerResponse> getWorker(@PathVariable String customerID) {
        APIresponse<CustomerResponse> reponse = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(customerService.getCustomerbyID(customerID));
        return reponse;
    }

    @PutMapping("/{customerID}")
    APIresponse<CustomerResponse> updateWorkerInfo(@PathVariable String customerID, @RequestBody CustomerUpdateRequest rq) {
        APIresponse<CustomerResponse> reponse = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(customerService.updateCustomer(customerID, rq));
        return reponse;
    }

    @DeleteMapping("/{customerID}")
    APIresponse<String> deleteWorker(@PathVariable String customerID) {
        customerService.deleteCustomer(customerID);
        APIresponse<String> response = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());
        response.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());
        return response;
    }
}
