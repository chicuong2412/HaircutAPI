package com.haircutAPI.HaircutAPI.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haircutAPI.HaircutAPI.enity.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>{

    boolean existsByUsername(String username);

    Optional<Customer> findByUsername(String username);
} 