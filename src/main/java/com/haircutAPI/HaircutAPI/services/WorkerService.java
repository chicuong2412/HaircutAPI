package com.haircutAPI.HaircutAPI.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerUpdateRequest;
import com.haircutAPI.HaircutAPI.dto.response.WorkerResponse;
import com.haircutAPI.HaircutAPI.enity.Worker;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.WorkerMapper;
import com.haircutAPI.HaircutAPI.repositories.WorkerRepository;

@Service
public class WorkerService {
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private WorkerMapper workerMapper;

    public WorkerResponse createWorker(WorkerCreationRequest request) {

        if (workerRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        Worker worker = workerMapper.toWorker(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        worker.setPassword(passwordEncoder.encode(request.getPassword()));
        return workerMapper.toWorkerResponse(workerRepository.save(worker));
    }

    public List<WorkerResponse> getAllWorkers() {
        return workerMapper.toWorkerResponses(workerRepository.findAll());
    }

    public WorkerResponse getWorkerbyID(String idWorker) {
        return workerMapper.toWorkerResponse(
                workerRepository.findById(idWorker).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND)));
    }

    public WorkerResponse updateWorker(String id, WorkerUpdateRequest rq) {

        if (!workerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);

        Worker worker = workerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        workerMapper.updateWorker(worker, rq);

        return workerMapper.toWorkerResponse(workerRepository.save(worker));
    }

    public void deleteWorker(String id) {
        if (!workerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        workerRepository.deleteById(id);
    }

    public List<WorkerResponse> searchByName(String name) {
        List<Worker> list = workerRepository.findAll();
        List<Worker> listResult = new ArrayList<>();
        list.forEach(t -> {
            if (t.getNameWorker().contains(name))
                listResult.add(t);
        });
        return workerMapper.toWorkerResponses(listResult);
    }
}
