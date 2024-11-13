package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haircutAPI.HaircutAPI.enity.Worker;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

}
