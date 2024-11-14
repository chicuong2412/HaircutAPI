package com.haircutAPI.HaircutAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.dto.request.WorkerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.WorkerUpdateRequest;
import com.haircutAPI.HaircutAPI.enity.Worker;
import com.haircutAPI.HaircutAPI.services.WorkerService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    @Autowired
    private WorkerService workerService;

    @PostMapping
    Worker createWorker(@RequestBody WorkerCreationRequest request) {
        return workerService.createWorker(request);
    }

    @GetMapping
    List<Worker> getWorkers() {
        return workerService.getAllWorkers();
    }

    @GetMapping("/{WorkerID}")
    public Worker getWorker(@PathVariable String WorkerID) {
        return workerService.getWorkerbyID(WorkerID);
    }

    @PutMapping("/{workerID}")
    public Worker updateWorkerInfo(@PathVariable String workerID, @RequestBody WorkerUpdateRequest rq) {
        return workerService.updateWorker(workerID, rq);
    }

    @DeleteMapping("/{workerID}")
    public void deleteWorker(@PathVariable String workerID) {
        workerService.deleteWorker(workerID);
    }

}
