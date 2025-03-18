package com.haircutAPI.HaircutAPI.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.Location;

public interface LocationRepository extends JpaRepository<Location, String> {

    List<Location> findByIsDeletedFalse();
}
