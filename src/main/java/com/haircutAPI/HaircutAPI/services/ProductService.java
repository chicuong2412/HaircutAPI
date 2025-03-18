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
import com.haircutAPI.HaircutAPI.dto.request.ProductRequest.ProductCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ProductRequest.ProductUpdatioRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.ProductResponse;
import com.haircutAPI.HaircutAPI.enity.Product;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.ProductMapper;
import com.haircutAPI.HaircutAPI.repositories.ProductRepository;
import com.haircutAPI.HaircutAPI.utils.ServicesUtils;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ServicesUtils servicesUtils;
    @Autowired
    ImagesUploadService imagesUploadService;

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<ProductResponse> createProduct(ProductCreationRequest rq) {
        Product product = new Product();
        product = productMapper.toProduct(product, rq);

        product.setId(servicesUtils.idGenerator("PO", "product"));

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
                product.setImgSrc(temp.getImgSrc());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        productRepository.save(product);

        APIresponse<ProductResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());

        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        rp.setResult(productMapper.toProductResponse(product));

        return rp;
    }

    @SuppressWarnings("finally")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<ProductResponse> updateProduct(ProductUpdatioRequest rq, String idProduct) {
        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        APIresponse<ProductResponse> rp = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());

        rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
        productMapper.updateProduct(product, rq);

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
                    if (product.getImgSrc() != null && !product.getImgSrc().equals("")) {
                        imagesUploadService.deleteFile(product.getImgSrc().split("=")[1].replace("&sz", ""));
                    }
                } catch (Exception e) {

                } finally {
                    var temp = imagesUploadService.uploadImageToGoogleDrive(file);
                    product.setImgSrc(temp.getImgSrc());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            productRepository.save(product);
            rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());
            rp.setResult(productMapper.toProductResponse(product));
            return rp;
        }
    }

    public APIresponse<ProductResponse> getProduct(String idProduct) {
        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        APIresponse<ProductResponse> rp = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());

        rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());

        rp.setResult(productMapper.toProductResponse(product));

        rp.getResult().setDeleted(product.isDeleted());

        return rp;
    }

    public APIresponse<List<ProductResponse>> getProducts() {
        APIresponse<List<ProductResponse>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());

        rp.setMessage(SuccessCode.GET_DATA_SUCCESSFUL.getMessage());

        rp.setResult(productMapper.toProductResponses(productRepository.findByIsDeletedFalse()));

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public void deleteProduct(String idProduct) {
        if (!productRepository.existsById(idProduct))
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        var product = productRepository.findById(idProduct).orElse(null);
        product.setDeleted(true);
        productRepository.save(product);
    }

}
