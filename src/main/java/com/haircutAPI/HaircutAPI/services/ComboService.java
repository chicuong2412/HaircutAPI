package com.haircutAPI.HaircutAPI.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.ComboRequest.ComboCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ComboRequest.ComboUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.ComboResponse;
import com.haircutAPI.HaircutAPI.enity.ComboEntity;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.ComboMapper;
import com.haircutAPI.HaircutAPI.repositories.ComboRepository;
import com.haircutAPI.HaircutAPI.utils.ServicesUtils;

@Service
public class ComboService {
    @Autowired
    ComboRepository comboRepository;
    @Autowired
    ComboMapper comboEntityMapper;
    @Autowired
    ServicesUtils servicesUtils;
    @Autowired
    ImagesUploadService imagesUploadService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<ComboResponse> createCombo(ComboCreationRequest rq) {
        APIresponse<ComboResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        ComboEntity comboEntity = new ComboEntity();
        comboEntity = comboEntityMapper.toComboEntity(comboEntity, rq);
        comboEntity.setId(servicesUtils.idGenerator("CO", "combo"));

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
                comboEntity.setImgSrc(temp.getImgSrc());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        comboRepository.save(comboEntity);

        rp.setResult(comboEntityMapper.toComboResponse(comboEntity));

        return rp;
    }

    @SuppressWarnings("finally")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<ComboResponse> updateCombo(ComboUpdationRequest rq, String idCombo) {

        ComboEntity comboEntity = comboRepository.findById(idCombo)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        APIresponse<ComboResponse> rp = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());

        comboEntityMapper.updateComboEntity(comboEntity, rq);
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
                    if (comboEntity.getImgSrc() != null && !comboEntity.getImgSrc().equals("")) {
                        imagesUploadService.deleteFile(comboEntity.getImgSrc().split("=")[1].replace("&sz", ""));
                    }
                } catch (Exception e) {
                } finally {
                    var temp = imagesUploadService.uploadImageToGoogleDrive(file);
                    comboEntity.setImgSrc(temp.getImgSrc());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            comboRepository.save(comboEntity);
            rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
            rp.setResult(comboEntityMapper.toComboResponse(comboEntity));
            return rp;
        }

    }

    public APIresponse<ComboResponse> getComboEntity(String idCombo) {
        ComboEntity comboEntity = comboRepository.findById(idCombo)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        APIresponse<ComboResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        rp.setResult(comboEntityMapper.toComboResponse(comboEntity));

        return rp;
    }

    public APIresponse<List<ComboResponse>> getAllComboEntity() {
        List<ComboEntity> serviceEntities = comboRepository.findAll();

        APIresponse<List<ComboResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        rp.setResult(comboEntityMapper.toComboResponses(serviceEntities));

        return rp;
    }

    public APIresponse<List<ComboResponse>> getAllPublicComboEntity() {
        List<ComboEntity> comboEntities = comboRepository.findByIsDeletedFalse();

        APIresponse<List<ComboResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());
        rp.setResult(comboEntityMapper.toComboResponses(comboEntities));

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteCombo(String idCombo) {
        if (!comboRepository.existsById(idCombo))
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        var combo = comboRepository.findById(idCombo).orElse(null);
        combo.setDeleted(true);
        comboRepository.save(combo);
    }
}
