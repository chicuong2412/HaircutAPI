package com.haircutAPI.HaircutAPI.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
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
import com.haircutAPI.HaircutAPI.dto.response.WorkerInfoPublicResponse;
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
    @Autowired
    ImagesUploadService imagesUploadService;

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
        if (request.getFile() != null && !request.getFile().equals("")) {
            byte[] bytes = Base64.getDecoder().decode(request.getFile());
            File file;
            try {
                file = File.createTempFile("temp", null);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
                fos.close();
                var temp = imagesUploadService.uploadImageToGoogleDrive(file);
                worker.setImgSrc(temp.getImgSrc());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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

    public List<WorkerInfoPublicResponse> getPublicWorkers() {
        List<Worker> workers = workerRepository.findAll();
        return servicesUtils.addAllLocationEntityPublic(workers);
    }

    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public WorkerResponse getWorkerbyID(String idWorker) {
        return servicesUtils.addLocationEntity(
                workerRepository.findById(idWorker).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND)));
    }

    @SuppressWarnings("finally")
    @PostAuthorize("returnObject.username == authentication.name or hasAuthority('SCOPE_ADMIN')")
    public WorkerResponse updateWorker(String id, WorkerUpdateRequest rq) {

        if (!workerRepository.existsById(id))
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        Worker worker = workerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        workerMapper.updateWorker(worker, rq);
        if (rq.getLocation() != null)
            worker.setIdLocation(rq.getLocation());

        try {
            if (rq.getFile() != null && !rq.getFile().equals("")) {
                byte[] bytes = Base64.getDecoder().decode(rq.getFile());
                File file;
                file = File.createTempFile("temp", null);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
                fos.close();
                try {
                    if (worker.getImgSrc() != null && !worker.getImgSrc().equals("")) {
                        imagesUploadService.deleteFile(worker.getImgSrc().split("=")[1].replace("&sz", ""));
                    }
                } catch (Exception e) {

                } finally {
                    var temp = imagesUploadService.uploadImageToGoogleDrive(file);
                    worker.setImgSrc(temp.getImgSrc());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return servicesUtils.addLocationEntity(workerRepository.save(worker));
        }

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

    public APIresponse<String> getMyAvatarSource(Authentication authen) {
        APIresponse<String> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        Worker worker = workerRepository.findByUsername(authen.getName());
        rp.setResult(worker.getImgSrc());
        return rp;
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
        return servicesUtils.addAllLocationEntity(listWorkerBeforeSearch);
    }

    public APIresponse<WorkerResponse> getMyInfo(Authentication authen) {
        APIresponse<WorkerResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        // System.out.println(authen.getName());
        Worker worker = workerRepository.findByUsername(authen.getName());
        rp.setResult(workerMapper.toWorkerResponse(worker));
        if (!authen.getName().equals("admin"))
            rp.getResult().setLocation(servicesUtils.findLocationByID(worker.getIdLocation()));
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
