package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.TokenFirebase;

public interface TokenRepository extends JpaRepository<TokenFirebase, Long> {
    
}
