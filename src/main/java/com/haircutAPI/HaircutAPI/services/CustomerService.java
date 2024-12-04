package com.haircutAPI.HaircutAPI.services;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.ENUM.UserType;
import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerUpdateRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.CustomerResponse;
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.enity.User;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.CustomerMapper;
import com.haircutAPI.HaircutAPI.repositories.CustomerRepository;
import com.haircutAPI.HaircutAPI.repositories.UserRepository;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    UserRepository userRepository;

    public CustomerResponse createCustomer(CustomerCreationRequest rq) {
        if (customerRepository.existsByUsername(rq.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = new User();

        user.setUsername(rq.getUsername());
        user.setPassword(passwordEncoder.encode(rq.getPassword()));
        
        HashSet<String> role = new HashSet<>();
        role.add(UserType.CUSTOMER.name());
        user.setRoles(role);
        userRepository.save(user);
        Customer customer = new Customer();
        customer = customerMapper.toCustomer(rq);

        customer.setPassword(passwordEncoder.encode(rq.getPassword()));
        customer.setId(user.getId());

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<CustomerResponse> getAllCustomers(String name) {
        return customerMapper.toCustomerResponses(customerRepository.filterByNameWorker(name, customerRepository.findAll()));
    }

    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public CustomerResponse getCustomerbyID(String idCustomer) {
        return customerMapper.toCustomerResponse(
                customerRepository.findById(idCustomer).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND)));
    }

    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public CustomerResponse updateCustomer(String id, CustomerUpdateRequest rq) {

        if (!customerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        customerMapper.updateCutomer(customer, rq);

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteCustomer(String id) {
        if (!customerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        customerRepository.deleteById(id);
    }

    public APIresponse<CustomerResponse> getMyInfo(Authentication authen) {
        APIresponse<CustomerResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        System.out.println(authen.getName());
        Customer customer = customerRepository.findByUsername(authen.getName()).orElseThrow();
        rp.setResult(customerMapper.toCustomerResponse(customer));
        return rp;
    }
}
