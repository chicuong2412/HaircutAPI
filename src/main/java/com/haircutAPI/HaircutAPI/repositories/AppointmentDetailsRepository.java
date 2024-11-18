package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haircutAPI.HaircutAPI.enity.AppointmentDetails;

@Repository
public interface AppointmentDetailsRepository extends JpaRepository<AppointmentDetails, String> {
    
}
