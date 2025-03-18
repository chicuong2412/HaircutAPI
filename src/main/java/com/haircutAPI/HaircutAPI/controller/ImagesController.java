package com.haircutAPI.HaircutAPI.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.enity.Images;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.services.ImagesUploadService;

@Controller
@RequestMapping("")
public class ImagesController {

    @Autowired
    ImagesUploadService imagesUploadService;

    @PostMapping("/uploadImage")
    public APIresponse<Images> handleFileUploading(@RequestParam("image") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new AppException(ErrorCode.DATA_INPUT_INVALID);
        }
        APIresponse<Images> rp = new APIresponse<Images>(SuccessCode.CREATE_SUCCESSFUL.getCode());
        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());
        File tempFile = File.createTempFile("temp", null);
        file.transferTo(tempFile);
        rp.setResult(imagesUploadService.uploadImageToGoogleDrive(tempFile));
        return rp;
    }
}
