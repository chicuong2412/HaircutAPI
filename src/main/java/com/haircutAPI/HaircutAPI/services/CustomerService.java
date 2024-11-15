package com.haircutAPI.HaircutAPI.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerUpdateRequest;
import com.haircutAPI.HaircutAPI.dto.response.CustomerResponse;
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.CustomerMapper;
import com.haircutAPI.HaircutAPI.repositories.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapper;

    public CustomerResponse createCustomer(CustomerCreationRequest rq) {

        if (customerRepository.existsByUsername(rq.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);

        Customer customer = customerMapper.toCustomer(rq);

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerMapper.toCustomerResponses(customerRepository.findAll());
    }

    public CustomerResponse getCustomerbyID(String idCustomer) {
        return customerMapper.toCustomerResponse(
                customerRepository.findById(idCustomer).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND)));
    }

    public CustomerResponse updateCustomer(String id, CustomerUpdateRequest rq) {

        if (!customerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        customerMapper.updateCutomer(customer, rq);

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    public void deleteCustomer(String id) {
        if (!customerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        customerRepository.deleteById(id);
    }

    public List<CustomerResponse> searchByName(String name) {
        List<Customer> list = customerRepository.findAll();
        List<Customer> listResult = new ArrayList<>();
        list.forEach(t -> {
            if (t.getNameCustomer().contains(name))
                listResult.add(t);
        });
        return customerMapper.toCustomerResponses(listResult);
    }
}
