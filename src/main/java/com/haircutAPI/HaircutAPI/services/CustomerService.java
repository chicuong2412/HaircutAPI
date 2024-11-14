package com.haircutAPI.HaircutAPI.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerUpdateRequest;
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.repositories.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public Customer createCustomer(CustomerCreationRequest rq) {
        Customer customer = new Customer();

        if (customerRepository.existsByUsername(rq.getUsername()))
            throw new RuntimeException("This username has already been used");

        customer.setUsername(rq.getUsername());
        customer.setPassword(rq.getPassword());
        customer.setNameCustomer(rq.getNameCustomer());
        customer.setEmail(rq.getEmail());
        customer.setAddress(rq.getAddress());
        customer.setPhoneNumber(rq.getPhoneNumber());
        customer.setLoyaltyPoint(rq.getLoyaltyPoint());
        customer.setDoB(rq.getDoB());
        customer.setStartDate(rq.getStartDate());
        customer.setLastDayUsing(rq.getLastDayUsing());
        customer.setTypeCustomer(rq.getTypeCustomer());

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerbyID(String idCustomer) {
        return customerRepository.findById(idCustomer).orElseThrow(() -> new RuntimeException("Worker not found"));
    }

    public Customer updateCustomer(String id, CustomerUpdateRequest rq) {

        if (!customerRepository.existsById(id))
            throw new RuntimeException("Worker ID is not found");

        Customer customer = getCustomerbyID(id);

        customer.setNameCustomer(rq.getNameCustomer());
        customer.setEmail(rq.getEmail());
        customer.setAddress(rq.getAddress());
        customer.setPhoneNumber(rq.getPhoneNumber());
        customer.setLoyaltyPoint(rq.getLoyaltyPoint());
        customer.setDoB(rq.getDoB());
        customer.setStartDate(rq.getStartDate());
        customer.setPassword(rq.getPassword());
        customer.setLastDayUsing(rq.getLastDayUsing());
        customer.setTypeCustomer(rq.getTypeCustomer());

        return customerRepository.save(customer);
    }

    public void deleteCustomer(String id) {
        if (!customerRepository.existsById(id))
            throw new RuntimeException("Customer ID is not found");
        customerRepository.deleteById(id);
    }

    public List<Customer> searchByName(String name) {
        List<Customer> list = getAllCustomers();
        List<Customer> listResult = new ArrayList<>();
        list.forEach(t -> {
            if (t.getNameCustomer().contains(name))
                listResult.add(t);
        });
        return listResult;
    }
}
