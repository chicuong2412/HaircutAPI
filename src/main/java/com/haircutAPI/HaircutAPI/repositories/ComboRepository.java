package com.haircutAPI.HaircutAPI.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.ComboEntity;

public interface ComboRepository extends JpaRepository<ComboEntity, String> {
    
    List<ComboEntity> findByIsDeletedFalse();
}
