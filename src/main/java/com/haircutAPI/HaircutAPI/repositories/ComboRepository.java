package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.ComboEntity;

public interface ComboRepository extends JpaRepository<ComboEntity, String> {
    
}
