package com.haircutAPI.HaircutAPI.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.repositories.CustomerRepository;
import com.haircutAPI.HaircutAPI.repositories.WorkerRepository;

@Service
public class ServicesUtils {
    
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    WorkerRepository workerRepository;

    public boolean isCustomerIdExisted(String customerID) {
        return customerRepository.existsById(customerID);
    }

    public boolean isWorkerIdExisted(String workerID) {
        return workerRepository.existsById(workerID);
    }


    
}
