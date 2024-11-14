package com.haircutAPI.HaircutAPI.services;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerUpdateRequest;
import com.haircutAPI.HaircutAPI.enity.Worker;
import com.haircutAPI.HaircutAPI.repositories.WorkerRepository;

@Service
public class WorkerService {
    @Autowired
    private WorkerRepository workerRepository;

    public Worker createWorker(WorkerCreationRequest request) {
        Worker worker = new Worker();

        if (workerRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("This username has already been used");

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
        worker.setUsername(request.getUsername());
        worker.setPassword(request.getPassword());

        return workerRepository.save(worker);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public Worker getWorkerbyID(String idWorker) {
        return workerRepository.findById(idWorker).orElseThrow(() -> new RuntimeException("Worker not found"));
    }

    public Worker updateWorker(String id, WorkerUpdateRequest rq) {

        if (!workerRepository.existsById(id))
            throw new RuntimeException("Worker ID is not found");

        Worker worker = getWorkerbyID(id);
        worker.setNameWorker(rq.getNameWorker());
        worker.setEmail(rq.getEmail());
        worker.setAddress(rq.getAddress());
        worker.setIdLocation(rq.getIdLocation());
        worker.setIdRole(rq.getIdRole());
        worker.setPhoneNumber(rq.getPhoneNumber());
        worker.setSalary(rq.getSalary());
        worker.setRate(rq.getRate());
        worker.setSpecialities(rq.getSpecialities());
        worker.setDoB(rq.getDoB());
        worker.setStartDate(rq.getStartDate());
        worker.setPassword(rq.getPassword());

        return workerRepository.save(worker);
    }

    public void deleteWorker(String id) {
        if (!workerRepository.existsById(id))
            throw new RuntimeException("Worker ID is not found");
        workerRepository.deleteById(id);
    }

    public List<Worker> searchByName(String name) {
        List<Worker> list = getAllWorkers();
        List<Worker> listResult = new ArrayList<>();
        list.forEach(t -> {
            if (t.getNameWorker().contains(name)) listResult.add(t);
        });
        return listResult;
    }
}
