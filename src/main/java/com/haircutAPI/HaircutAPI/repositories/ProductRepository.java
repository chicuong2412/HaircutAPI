package com.haircutAPI.HaircutAPI.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByIsDeletedFalse();
}
