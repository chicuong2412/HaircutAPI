package com.haircutAPI.HaircutAPI.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerUpdateRequest;
import com.haircutAPI.HaircutAPI.dto.response.WorkerInfoPublicResponse;
import com.haircutAPI.HaircutAPI.dto.response.WorkerResponse;
import com.haircutAPI.HaircutAPI.enity.Worker;


@Mapper(componentModel = "spring")
public interface WorkerMapper {

    Worker toWorker(WorkerCreationRequest rq);

    void updateWorker(@MappingTarget Worker worker, WorkerUpdateRequest rq);   
    
    List<WorkerResponse> toWorkerResponses(List<Worker> workers);

    List<WorkerInfoPublicResponse> toWorkerPublicResponses(List<Worker> workers);

    WorkerResponse toWorkerResponse(Worker worker);
}
