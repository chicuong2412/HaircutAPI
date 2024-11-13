package com.haircutAPI.HaircutAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.dto.request.WorkerCreationRequest;
import com.haircutAPI.HaircutAPI.enity.Worker;
import com.haircutAPI.HaircutAPI.repositories.WorkerRepository;

@Service
public class WorkerService {
    @Autowired
    private WorkerRepository workerRepository;

    public Worker createWorker(WorkerCreationRequest request) {
        Worker worker = new Worker();

        worker.setNameWorker(request.getNameWorker());
        worker.setEmail(request.getEmail());
        worker.setAddress(request.getAddress());
        worker.setIdLocation(request.getIdLocation());
        worker.setIdRole(request.getIdRole());
        worker.setPhoneNumber(request.getPhoneNumber());
        worker.setSalary(request.getSalary());
        worker.setRate(request.getRate());
        worker.setSpecialities(request.getSpecialities());
        worker.setDoB(request.getDoB());
        worker.setStartDate(request.getStartDate());


        return workerRepository.save(worker);
    }
}
