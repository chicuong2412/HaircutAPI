package com.haircutAPI.HaircutAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.dto.request.WorkerCreationRequest;
import com.haircutAPI.HaircutAPI.enity.Worker;
import com.haircutAPI.HaircutAPI.services.WorkerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class WorkerController {
    @Autowired
    private WorkerService workerService;

    @PostMapping("/workers")
    Worker createWorker(@RequestBody WorkerCreationRequest request) {
        return workerService.createWorker(request);
    }

}
