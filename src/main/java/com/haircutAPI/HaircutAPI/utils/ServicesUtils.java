package com.haircutAPI.HaircutAPI.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.repositories.ComboRepository;
import com.haircutAPI.HaircutAPI.repositories.CustomerRepository;
import com.haircutAPI.HaircutAPI.repositories.LocationRepository;
import com.haircutAPI.HaircutAPI.repositories.ProductRepository;
import com.haircutAPI.HaircutAPI.repositories.ServiceRepository;
import com.haircutAPI.HaircutAPI.repositories.WorkerRepository;

@Service
public class ServicesUtils {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    ComboRepository comboRepository;
    @Autowired
    ProductRepository productRepository;

    public boolean isCustomerIdExisted(String customerID) {
        return customerRepository.existsById(customerID);
    }

    public boolean isWorkerIdExisted(String workerID) {
        return workerRepository.existsById(workerID);
    }

    public boolean isProductIdExisted(String idProduct) {
        return productRepository.existsById(idProduct);
    }

    public boolean isLocationIdExisted(String idLocation) {
        return locationRepository.existsById(idLocation);
    }

    public boolean isIdServicesExisted(List<String> idServices) {
        for (String idService : idServices) {
            if (!serviceRepository.existsById(idService))
                return false;
        }
        return true;
    }

    public boolean isIdCombosExisted(List<String> idCombos) {
        for (String idCombo : idCombos) {
            if (!comboRepository.existsById(idCombo))
                return false;
        }
        return true;
    }

    public boolean isIdProductsExisted(List<String> idProducts) {
        for (String idProduct : idProducts) {
            if (!productRepository.existsById(idProduct))
                return false;
        }
        return true;
    }

    public Customer findCustomerByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
    }

    public String findCustomerIDByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        return customer.getId();
    }

}
