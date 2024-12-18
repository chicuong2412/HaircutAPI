package com.haircutAPI.HaircutAPI.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.enity.ComboEntity;
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.enity.ServiceEntity;
import com.haircutAPI.HaircutAPI.enity.Worker;
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

    public HashSet<ServiceEntity> toServiceEntitiesSet(Set<String> listServices) {
        return new HashSet<>(serviceRepository.findAllById(listServices));
    }

    public HashSet<ComboEntity> toComboEnitiesSet(Set<String> listCombos) {
        return new HashSet<>(comboRepository.findAllById(listCombos));
    }

    public Customer getCustomerByID(String id) {
        return customerRepository.findById(id).orElseThrow();
    }

    public boolean checkAuthoritesHasRole(Collection<GrantedAuthority> auts, String role) {
        for (GrantedAuthority currRole : auts) {
            if (currRole.getAuthority().compareTo(role) == 0) {
                return true;
            }
                
        }

        return false;
    }

    public String getWorkerIdLocation(String username) {
        return workerRepository.findByUsername(username).getIdLocation();
    }

    public Worker findWorkerByUsername(String username) {
        return workerRepository.findByUsername(username);
    }

    public Worker findWorkerById(String username) {
        return workerRepository.findById(username).orElseThrow(() -> new AppException(ErrorCode.ID_WORKER_NOT_FOUND));
    }
}
