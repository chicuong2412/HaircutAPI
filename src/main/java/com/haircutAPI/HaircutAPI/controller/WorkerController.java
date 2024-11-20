package com.haircutAPI.HaircutAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerUpdateRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.WorkerResponse;
import com.haircutAPI.HaircutAPI.services.WorkerService;

import jakarta.validation.Valid;

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
    APIresponse<WorkerResponse> createWorker(@RequestBody @Valid WorkerCreationRequest request) {
        APIresponse<WorkerResponse> reponse = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());
        reponse.setResult(workerService.createWorker(request));
        return reponse;
    }

    @GetMapping
    APIresponse<List<WorkerResponse>> getWorkers() {
        APIresponse<List<WorkerResponse>> reponse = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(workerService.getAllWorkers());
        return reponse;
    }

    @GetMapping("/searchName/{name}")
    APIresponse<List<WorkerResponse>> getListSearchByName(@PathVariable String name) {
        APIresponse<List<WorkerResponse>> reponse = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(workerService.searchByName(name));
        return reponse;
    }

    @GetMapping("/{WorkerID}")
    APIresponse<WorkerResponse> getWorker(@PathVariable String WorkerID) {
        APIresponse<WorkerResponse> reponse = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(workerService.getWorkerbyID(WorkerID));
        return reponse;
    }

    @PutMapping("/{workerID}")
    APIresponse<WorkerResponse> updateWorkerInfo(@PathVariable String workerID, @RequestBody WorkerUpdateRequest rq) {
        APIresponse<WorkerResponse> reponse = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());
        reponse.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
        reponse.setResult(workerService.updateWorker(workerID, rq));
        return reponse;
    }

    @DeleteMapping("/{workerID}")
    APIresponse<String> deleteWorker(@PathVariable String workerID) {
        workerService.deleteWorker(workerID);
        APIresponse<String> response = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());
        response.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());
        return response;
    }

}
