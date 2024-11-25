package com.haircutAPI.HaircutAPI.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haircutAPI.HaircutAPI.enity.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{

    boolean existsByUsername(String username);

    Optional<Customer> findByUsername(String username);

    default List<Customer> filterByNameWorker(String name, List<Customer> listCustomers) {
        List<Customer> workers = new ArrayList<>();
        for (Customer customer: listCustomers) {
            if (customer.getNameCustomer().indexOf(name) != -1) {
                workers.add(customer);
            }
        }
        return workers;
    }
} 