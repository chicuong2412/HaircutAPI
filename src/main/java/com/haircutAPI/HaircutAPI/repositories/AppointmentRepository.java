package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {

}
