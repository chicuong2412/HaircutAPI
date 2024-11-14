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

import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerUpdateRequest;
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    
    @Autowired
    CustomerService customerService;

    @PostMapping
    Customer createWorker(@RequestBody CustomerCreationRequest request) {
        return customerService.createCustomer(request);
    }

    @GetMapping
    List<Customer> getWorkers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/searchName/{name}")
    public List<Customer> getListSearchByName(@PathVariable String name) {
        return customerService.searchByName(name);
    }

    @GetMapping("/{customerID}")
    public Customer getWorker(@PathVariable String customerID) {
        return customerService.getCustomerbyID(customerID);
    }

    @PutMapping("/{customerID}")
    public Customer updateWorkerInfo(@PathVariable String customerID, @RequestBody CustomerUpdateRequest rq) {
        return customerService.updateCustomer(customerID, rq);
    }

    @DeleteMapping("/{customerID}")
    public void deleteWorker(@PathVariable String customerID) {
        customerService.deleteCustomer(customerID);
    }
}
