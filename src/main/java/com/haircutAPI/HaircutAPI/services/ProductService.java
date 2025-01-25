package com.haircutAPI.HaircutAPI.services;

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

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<ProductResponse> createProduct(ProductCreationRequest rq) {
        Product product = new Product();
        product = productMapper.toProduct(product, rq);

        product.setId(servicesUtils.idGenerator("PO", "product"));

        productRepository.save(product);

        APIresponse<ProductResponse> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());

        rp.setMessage(SuccessCode.CREATE_SUCCESSFUL.getMessage());

        rp.setResult(productMapper.toProductResponse(product));

        return rp;
    }

    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public APIresponse<ProductResponse> updateProduct(ProductUpdatioRequest rq, String idProduct) {
        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        productMapper.updateProduct(product, rq);
        productRepository.save(product);
        APIresponse<ProductResponse> rp = new APIresponse<>(SuccessCode.UPDATE_DATA_SUCCESSFUL.getCode());

        rp.setMessage(SuccessCode.UPDATE_DATA_SUCCESSFUL.getMessage());

        rp.setResult(productMapper.toProductResponse(product));

        return rp;
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

        rp.setResult(productMapper.toProductResponses(productRepository.findAll()));

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
