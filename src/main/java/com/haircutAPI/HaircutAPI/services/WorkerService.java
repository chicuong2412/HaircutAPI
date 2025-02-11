package com.haircutAPI.HaircutAPI.services;

import java.time.LocalDate;
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
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.ENUM.UserType;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.WorkerRequest.WorkerUpdateRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.CustomerResponse;
import com.haircutAPI.HaircutAPI.dto.response.WorkerInfoPublicResponse;
import com.haircutAPI.HaircutAPI.dto.response.WorkerResponse;
import com.haircutAPI.HaircutAPI.enity.Customer;
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

        checkWorkerCreationRq(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        HashSet<String> role = new HashSet<>();
        role.add(UserType.WORKER.name());
        user.setRoles(role);

        user.setId(servicesUtils.idGenerator("WOR", "worker"));

        userRepository.save(user);
        Worker worker = new Worker();
        worker = workerMapper.toWorker(worker, request);
        worker.setIdLocation(request.getLocation());
        worker.setStartDate(LocalDate.now());
        worker.setPassword(passwordEncoder.encode(request.getPassword()));
        worker.setId(user.getId());
        workerRepository.save(worker);

        return servicesUtils.addLocationEntity(worker);
    }

    @SuppressWarnings("unchecked")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN') or hasAuthority('SCOPE_MANAGER')")
    public List<WorkerResponse> getAllWorkers(String name, Authentication authentication) {

        if (!servicesUtils.checkAuthoritesHasRole((Collection<GrantedAuthority>) authentication.getAuthorities(),
                "SCOPE_ADMIN")) {
            Worker workerManager = servicesUtils.findWorkerByUsername(authentication.getName());
            List<Worker> listWorkerBeforeSearch = workerRepository.findAllByIdLocation(workerManager.getIdLocation());
            return servicesUtils
                    .addAllLocationEntity(workerRepository.filterByNameWorker(name, listWorkerBeforeSearch));
        }

        return servicesUtils
                .addAllLocationEntity(workerRepository.filterByNameWorker(name, workerRepository.findAll()));
    }

    public List<WorkerInfoPublicResponse> getPublicWorkersByIdLocation(String idLocation) {
        return workerMapper.toWorkerPublicResponses(workerRepository.findByIdLocationAndIsDeletedFalse(idLocation));
    }

    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public WorkerResponse getWorkerbyID(String idWorker) {
        return servicesUtils.addLocationEntity(
                workerRepository.findById(idWorker).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND)));
    }

    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public WorkerResponse updateWorker(String id, WorkerUpdateRequest rq) {

        if (!workerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);

        Worker worker = workerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        String password = worker.getPassword();
        workerMapper.updateWorker(worker, rq);
        worker.setIdLocation(rq.getLocation());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (rq.getPassword() != null) {
            worker.setPassword(passwordEncoder.encode(rq.getPassword()));
            userRepository.findById(id).orElseThrow().setPassword(passwordEncoder.encode(rq.getPassword()));
        } else {
            worker.setPassword(password);
        }

        return servicesUtils.addLocationEntity(workerRepository.save(worker));
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteWorker(String id) {
        if (!workerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        Worker worker = workerRepository.findById(id).orElse(null);
        worker.setDeleted(true);
        workerRepository.save(worker);
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
        return servicesUtils.addAllLocationEntity(workerRepository.filterByNameWorker(name, listWorkerBeforeSearch));
    }

     public APIresponse<WorkerResponse> getMyInfo(Authentication authen) {
        APIresponse<WorkerResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        System.out.println(authen.getName());
        Worker worker = workerRepository.findByUsername(authen.getName());
        rp.setResult(workerMapper.toWorkerResponse(worker));
        return rp;
    }

    private boolean checkWorkerCreationRq(WorkerCreationRequest rq) {
        if (workerRepository.existsByUsername(rq.getUsername()))
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        if (!servicesUtils.isLocationIdExisted(rq.getLocation()))
            throw new AppException(ErrorCode.ID_LOCATION_NOT_FOUND);
        return true;
    }


}
