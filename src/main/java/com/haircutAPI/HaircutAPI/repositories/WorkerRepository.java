package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haircutAPI.HaircutAPI.enity.Worker;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface WorkerRepository extends JpaRepository<Worker, String> {


    boolean existsByUsername(String username);

    List<Worker> findByNameWorker(String nameWorker);

    Worker findByUsername(String username);

    List<Worker> findByIsDeletedFalse();

    List<Worker> findByIdLocationAndIsDeletedFalse(String idLocation);

    List<Worker> findAllByIdLocation(String idLocation);

    default List<Worker> filterByNameWorker(String name, List<Worker> listWorkers) {
        List<Worker> workers = new ArrayList<>();
        for (Worker worker: listWorkers) {
            if (!worker.getUsername().equals("admin") && worker.getNameWorker().indexOf(name) != -1) {
                workers.add(worker);
            }
        }
        return workers;
    }
}
