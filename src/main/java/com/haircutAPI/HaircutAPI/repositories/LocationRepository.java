package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.Location;

public interface LocationRepository extends JpaRepository<Location, String> {

    
}
