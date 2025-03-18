package com.haircutAPI.HaircutAPI.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.ServiceRequest.ServiceCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ServiceRequest.ServiceUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.ServiceResponse;
import com.haircutAPI.HaircutAPI.enity.ServiceEntity;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.ServiceEntityMapper;
import com.haircutAPI.HaircutAPI.repositories.ProductRepository;
import com.haircutAPI.HaircutAPI.repositories.ServiceRepository;
import com.haircutAPI.HaircutAPI.utils.ServicesUtils;

@Service
public class ServiceEntityService {

    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    ServiceEntityMapper serviceEntityMapper;
    @Autowired
    ServicesUtils servicesUtils;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ImagesUploadService imagesUploadService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<ServiceResponse> createService(ServiceCreationRequest rq) {
        APIresponse<ServiceResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        var products = productRepository.findAllById(rq.getProductsList());

        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity = serviceEntityMapper.toServiceEntity(serviceEntity, rq);

        serviceEntity.setProductsList(new HashSet<>(products));

        serviceEntity.setId(servicesUtils.idGenerator("SE", "service"));

        if (rq.getFile() != null && !rq.getFile().equals("")) {
            byte[] bytes = Base64.getDecoder().decode(rq.getFile());
            File file;
            try {
                file = File.createTempFile("temp", null);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
                fos.close();
                var temp = imagesUploadService.uploadImageToGoogleDrive(file);
                serviceEntity.setImgSrc(temp.getImgSrc());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        serviceRepository.save(serviceEntity);

        rp.setResult(serviceEntityMapper.toServiceResponse(serviceEntity));

        return rp;
    }

    @SuppressWarnings("finally")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<ServiceResponse> updateService(ServiceUpdationRequest rq, String idService) {

        ServiceEntity serviceEntity = serviceRepository.findById(idService)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        APIresponse<ServiceResponse> rp = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());

        var products = productRepository.findAllById(rq.getProductsList());

        serviceEntityMapper.updateServiceEntity(serviceEntity, rq);

        serviceEntity.setProductsList(new HashSet<>(products));

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
                    if (serviceEntity.getImgSrc() != null && !serviceEntity.getImgSrc().equals("")) {
                        imagesUploadService.deleteFile(serviceEntity.getImgSrc().split("=")[1].replace("&sz", ""));
                    }
                } catch (Exception e) {

                } finally {
                    var temp = imagesUploadService.uploadImageToGoogleDrive(file);
                    serviceEntity.setImgSrc(temp.getImgSrc());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serviceRepository.save(serviceEntity);
            rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
            rp.setResult(serviceEntityMapper.toServiceResponse(serviceEntity));
            return rp;
        }
    }

    public APIresponse<ServiceResponse> getServiceEntity(String idService) {
        ServiceEntity serviceEntity = serviceRepository.findById(idService)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        System.out.println(serviceEntity.getDescription());
        APIresponse<ServiceResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        rp.setResult(serviceEntityMapper.toServiceResponse(serviceEntity));

        return rp;
    }

    public APIresponse<List<ServiceResponse>> getAllServiceEntity() {
        List<ServiceEntity> serviceEntities = serviceRepository.findAll();

        APIresponse<List<ServiceResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        rp.setResult(serviceEntityMapper.toServiceResponses(serviceEntities));

        return rp;
    }

    public APIresponse<List<ServiceResponse>> getAllPublicServiceEntity() {
        List<ServiceEntity> serviceEntities = serviceRepository.findByIsDeletedFalse();

        APIresponse<List<ServiceResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        rp.setResult(serviceEntityMapper.toServiceResponses(serviceEntities));

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteServiceEntity(String idService) {

        if (!serviceRepository.existsById(idService))
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        var service = serviceRepository.findById(idService).orElse(null);
        service.setDeleted(true);
        serviceRepository.save(service);
    }
}
