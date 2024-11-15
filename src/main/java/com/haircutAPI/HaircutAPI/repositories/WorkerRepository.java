package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haircutAPI.HaircutAPI.enity.Worker;
import java.util.List;


@Repository
public interface WorkerRepository extends JpaRepository<Worker, String> {


    boolean existsByUsername(String username);

    List<Worker> findByNameWorker(String nameWorker);
}
