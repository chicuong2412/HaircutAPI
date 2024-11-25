package com.haircutAPI.HaircutAPI.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.ENUM.UserType;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerUpdateRequest;
import com.haircutAPI.HaircutAPI.dto.response.WorkerResponse;
import com.haircutAPI.HaircutAPI.enity.User;
import com.haircutAPI.HaircutAPI.enity.Worker;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.WorkerMapper;
import com.haircutAPI.HaircutAPI.repositories.UserRepository;
import com.haircutAPI.HaircutAPI.repositories.WorkerRepository;
import com.haircutAPI.HaircutAPI.utils.ServicesUtils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkerService {
    @Autowired
    WorkerRepository workerRepository;
    @Autowired
    WorkerMapper workerMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ServicesUtils servicesUtils;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public WorkerResponse createWorker(WorkerCreationRequest request) {

        if (!checkWorkerCreationRq(request))
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> role = new HashSet<>();
        role.add(UserType.WORKER.name());
        user.setRoles(role);
        userRepository.save(user);
        Worker worker = workerMapper.toWorker(request);
        System.out.println(worker.getIdRole());

        worker.setPassword(passwordEncoder.encode(request.getPassword()));
        worker.setId(user.getId());

        return workerMapper.toWorkerResponse(workerRepository.save(worker));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<WorkerResponse> getAllWorkers(String name) {
        return workerMapper.toWorkerResponses(workerRepository.filterByNameWorker(name, workerRepository.findAll()));
    }

    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public WorkerResponse getWorkerbyID(String idWorker) {
        return workerMapper.toWorkerResponse(
                workerRepository.findById(idWorker).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND)));
    }

    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public WorkerResponse updateWorker(String id, WorkerUpdateRequest rq) {

        if (!workerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);

        Worker worker = workerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        workerMapper.updateWorker(worker, rq);

        return workerMapper.toWorkerResponse(workerRepository.save(worker));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteWorker(String id) {
        if (!workerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        workerRepository.deleteById(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public List<WorkerResponse> searchByName(String name) {
        List<Worker> list = workerRepository.findAll();
        List<Worker> listResult = new ArrayList<>();
        list.forEach(t -> {
            if (t.getNameWorker().contains(name))
                listResult.add(t);
        });
        return workerMapper.toWorkerResponses(listResult);
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MANAGER')")
    public List<WorkerResponse> getWorkersByIdLocation(String idLocation, String name, Authentication authentication) {
        if (!servicesUtils.checkAuthoritesHasRole((Collection<GrantedAuthority>) authentication.getAuthorities(),
                "SCOPE_ADMIN")) {
            if (!servicesUtils.findWorkerByUsername(authentication.getName()).getIdLocation().equals(idLocation)) {
                throw new AccessDeniedException("Access Denied");
            }
        }
        List<Worker> listWorkerBeforeSearch = workerRepository.findAllByIdLocation(idLocation);
        return workerMapper.toWorkerResponses(workerRepository.filterByNameWorker(name, listWorkerBeforeSearch));
    }

    private boolean checkWorkerCreationRq(WorkerCreationRequest rq) {
        if (workerRepository.existsByUsername(rq.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        return true;
    }
}
