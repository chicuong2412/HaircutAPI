package com.haircutAPI.HaircutAPI.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.CustomerRequest.CustomerUpdateRequest;
import com.haircutAPI.HaircutAPI.dto.response.CustomerResponse;
import com.haircutAPI.HaircutAPI.enity.Customer;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

    Customer toCustomer(@MappingTarget Customer customer, CustomerCreationRequest rq);

    void updateCutomer(@MappingTarget Customer customer, CustomerUpdateRequest rq);

    List<CustomerResponse> toCustomerResponses(List<Customer> customers);

    CustomerResponse toCustomerResponse(Customer customer);
}
