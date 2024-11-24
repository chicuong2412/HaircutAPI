package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, String> {
    
}
