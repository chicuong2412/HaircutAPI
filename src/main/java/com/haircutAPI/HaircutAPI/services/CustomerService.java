package com.haircutAPI.HaircutAPI.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
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
import com.haircutAPI.HaircutAPI.utils.ServicesUtils;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ServicesUtils servicesUtils;
    @Autowired
    ImagesUploadService imagesUploadService;

    public CustomerResponse createCustomer(CustomerCreationRequest rq) {
        if (userRepository.existsByUsername(rq.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = new User();

        user.setUsername(rq.getUsername());
        user.setPassword(passwordEncoder.encode(rq.getPassword()));

        HashSet<String> role = new HashSet<>();
        role.add(UserType.CUSTOMER.name());
        user.setRoles(role);

        user.setId(servicesUtils.idGenerator("CUS", "customer"));

        userRepository.save(user);
        Customer customer = new Customer();
        customer = customerMapper.toCustomer(customer, rq);
        if (rq.getFile() != null && !rq.getFile().equals("")) {
            byte[] bytes = Base64.getDecoder().decode(rq.getFile());
            File file;
            try {
                file = File.createTempFile("temp", null);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
                fos.close();
                var temp = imagesUploadService.uploadImageToGoogleDrive(file);
                customer.setImgSrc(temp.getImgSrc());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        // customer.setPassword(passwordEncoder.encode(rq.getPassword()));
        customer.setId(user.getId());
        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    public CustomerResponse createCustomerAdmin(CustomerCreationRequest rq) {
        if (customerRepository.existsByUsername(rq.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = new User();

        user.setUsername(rq.getUsername());
        user.setPassword(passwordEncoder.encode(rq.getPassword()));

        HashSet<String> role = new HashSet<>();
        role.add(UserType.CUSTOMER.name());
        user.setRoles(role);

        user.setId(servicesUtils.idGenerator("CUS", "customer"));

        userRepository.save(user);
        Customer customer = new Customer();
        customer = customerMapper.toCustomer(customer, rq);

        // customer.setPassword(passwordEncoder.encode(rq.getPassword()));
        customer.setId(user.getId());
        customer.setAddress("");
        LocalDate date = LocalDate.now();
        customer.setStartDate(date);
        customer.setDoB(date);
        customer.setPhoneNumber("");

        return customerMapper.toCustomerResponse(customerRepository.save(customer));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MANAGER')")
    public List<CustomerResponse> getAllCustomers(String name) {
        return customerMapper
                .toCustomerResponses(customerRepository.filterByNameWorker(name, customerRepository.findAll()));
    }

    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public CustomerResponse getCustomerbyID(String idCustomer) {
        return customerMapper.toCustomerResponse(
                customerRepository.findById(idCustomer).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND)));
    }

    @SuppressWarnings("finally")
    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public CustomerResponse updateCustomer(String id, CustomerUpdateRequest rq) {

        if (!customerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        customerMapper.updateCutomer(customer, rq);
        try {
            if (rq.getFile() != null && !rq.getFile().equals("")) {
                byte[] bytes = Base64.getDecoder().decode(rq.getFile());
                File file;
                file = File.createTempFile("temp", null);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
                fos.close();
                try {
                    if (customer.getImgSrc() != null && !customer.getImgSrc().equals("")) {
                        imagesUploadService.deleteFile(customer.getImgSrc().split("=")[1].replace("&sz", ""));
                    }
                } catch (Exception e) {

                } finally {
                    var temp = imagesUploadService.uploadImageToGoogleDrive(file);
                    customer.setImgSrc(temp.getImgSrc());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return customerMapper.toCustomerResponse(customerRepository.save(customer));
        }

    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteCustomer(String id) {
        if (!customerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);

        var customer = customerRepository.findById(id).orElse(null);
        customer.setDeleted(true);
        customerRepository.save(customer);
    }

    public APIresponse<CustomerResponse> getMyInfo(Authentication authen) {
        APIresponse<CustomerResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        System.out.println(authen.getName());
        Customer customer = customerRepository.findByUsername(authen.getName()).orElseThrow();
        rp.setResult(customerMapper.toCustomerResponse(customer));
        return rp;
    }

    public APIresponse<String> getMyAvatarSource(Authentication authen) {
        APIresponse<String> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        Customer customer = customerRepository.findByUsername(authen.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        rp.setResult(customer.getImgSrc());
        return rp;
    }
}
