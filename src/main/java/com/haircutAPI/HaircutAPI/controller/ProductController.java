package com.haircutAPI.HaircutAPI.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.ProductRequest.ProductCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ProductRequest.ProductUpdatioRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.ProductResponse;
import com.haircutAPI.HaircutAPI.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping()
    public APIresponse<ProductResponse> createService(@RequestBody @Valid ProductCreationRequest rq) {
        return productService.createProduct(rq);
    }

    @PutMapping("/{id}")
    public APIresponse<ProductResponse> updateService(@PathVariable String id, @RequestBody @Valid ProductUpdatioRequest rq) {
        return productService.updateProduct(rq, id);
    }


    @GetMapping("/{id}")
    public APIresponse<ProductResponse> getService(@PathVariable String id) {
        return productService.getProduct(id);
    }

    @GetMapping("/getAllServices")
    public APIresponse<List<ProductResponse>> getAllServices() {
        return productService.getProducts();
    }
    
    @DeleteMapping("/{idProduct}")
    APIresponse<String> deleteCustomer(@PathVariable String idProduct) {
        productService.deleteProduct(idProduct);
        APIresponse<String> response = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());
        response.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());
        return response;
    }
    
}
