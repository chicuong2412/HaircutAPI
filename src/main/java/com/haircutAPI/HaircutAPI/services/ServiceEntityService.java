package com.haircutAPI.HaircutAPI.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.haircutAPI.HaircutAPI.repositories.ServiceRepository;

@Service
public class ServiceEntityService {

    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    ServiceEntityMapper serviceEntityMapper;

    public APIresponse<ServiceResponse> createService(ServiceCreationRequest rq) {
        APIresponse<ServiceResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        ServiceEntity serviceEntity = serviceEntityMapper.toServiceEntity(rq);

        serviceRepository.save(serviceEntity);

        rp.setResult(serviceEntityMapper.toServiceResponse(serviceEntity));

        return rp;
    }

    public APIresponse<ServiceResponse> updateService(ServiceUpdationRequest rq, String idService) {

        ServiceEntity serviceEntity = serviceRepository.findById(idService)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        APIresponse<ServiceResponse> rp = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());

        serviceEntityMapper.updateServiceEntity(serviceEntity, rq);

        serviceRepository.save(serviceEntity);

        rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());

        rp.setResult(serviceEntityMapper.toServiceResponse(serviceEntity));

        return rp;
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

    public void deleteServiceEntity(String idService) {
        serviceRepository.deleteById(idService);
    }
}
