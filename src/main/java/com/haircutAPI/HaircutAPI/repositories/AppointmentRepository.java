package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.Appointment;
import java.util.List;


public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    
    List<Appointment> findByIdCustomer(String idCustomer);

    List<Appointment> findAllByIdLocation(String idLocation);

    List<Appointment> findAllByIdWorker(String idWorker);

    


}
