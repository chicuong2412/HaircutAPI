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
import com.haircutAPI.HaircutAPI.dto.request.LocationRequest.LocationCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.LocationRequest.LocationUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.LocationResponse;
import com.haircutAPI.HaircutAPI.enity.Location;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.LocationMapper;
import com.haircutAPI.HaircutAPI.repositories.LocationRepository;
import com.haircutAPI.HaircutAPI.utils.ServicesUtils;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    LocationMapper locationMapper;

    @Autowired
    ServicesUtils servicesUtils;

    @Autowired
    ImagesUploadService imagesUploadService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<LocationResponse> createLocation(LocationCreationRequest rq) {
        APIresponse<LocationResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        Location location = new Location();
        location = locationMapper.toLocation(location, rq);
        location.setId(servicesUtils.idGenerator("LO", "location"));

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
                location.setImgSrc(temp.getImgSrc());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        locationRepository.save(location);

        rp.setResult(locationMapper.toLocationResponse(location));

        return rp;
    }

    @SuppressWarnings("finally")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<LocationResponse> updateLocation(LocationUpdationRequest rq, String idLocation) {
        Location location = locationRepository.findById(idLocation)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        APIresponse<LocationResponse> rp = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
        locationMapper.updateLocation(location, rq);
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
                    if (location.getImgSrc() != null && !location.getImgSrc().equals("")) {
                        imagesUploadService.deleteFile(location.getImgSrc().split("=")[1].replace("&sz", ""));
                    }
                } catch (Exception e) {

                } finally {
                    var temp = imagesUploadService.uploadImageToGoogleDrive(file);
                    location.setImgSrc(temp.getImgSrc());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            locationRepository.save(location);
            rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
            rp.setResult(locationMapper.toLocationResponse(location));
            return rp;
        }
    }

    public APIresponse<LocationResponse> getLocation(String idLocation) {
        Location location = locationRepository.findById(idLocation)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        APIresponse<LocationResponse> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        rp.setResult(locationMapper.toLocationResponse(location));

        return rp;
    }

    public APIresponse<List<LocationResponse>> getLocations() {

        APIresponse<List<LocationResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        rp.setResult(locationMapper.toLocationResponses(locationRepository.findAll()));

        return rp;
    }

    public APIresponse<List<LocationResponse>> getPublicLocations() {

        APIresponse<List<LocationResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        rp.setResult(locationMapper.toLocationResponses(locationRepository.findByIsDeletedFalse()));

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<String> deleteLocation(String idLocation) {

        if (!locationRepository.existsById(idLocation))
            throw new AppException(ErrorCode.ID_NOT_FOUND);

        APIresponse<String> rp = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());

        rp.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());

        var location = locationRepository.findById(idLocation).orElse(null);

        location.setDeleted(true);

        locationRepository.save(location);

        return rp;
    }
}
