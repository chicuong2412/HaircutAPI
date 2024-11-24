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
import com.haircutAPI.HaircutAPI.dto.request.ComboRequest.ComboCreationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ComboRequest.ComboUpdationRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.ComboResponse;
import com.haircutAPI.HaircutAPI.services.ComboService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/combo")
public class ComboController {
    @Autowired
    ComboService comboEntityService;

    @PostMapping()
    public APIresponse<ComboResponse> createService(@RequestBody @Valid ComboCreationRequest rq) {
        return comboEntityService.createCombo(rq);
    }

    @PutMapping("/{id}")
    public APIresponse<ComboResponse> updateService(@PathVariable String id, @RequestBody @Valid ComboUpdationRequest rq) {
        return comboEntityService.updateCombo(rq, id);
    }


    @GetMapping("/{id}")
    public APIresponse<ComboResponse> getService(@PathVariable String id) {
        return comboEntityService.getComboEntity(id);
    }

    @GetMapping("/getAllCombo")
    public APIresponse<List<ComboResponse>> getAllServices() {
        return comboEntityService.getAllComboEntity();
    }
    
    @DeleteMapping("/{idCombo}")
    APIresponse<String> deleteCustomer(@PathVariable String idCombo) {
        comboEntityService.deleteCombo(idCombo);
        APIresponse<String> response = new APIresponse<>(SuccessCode.DELETE_SUCCESSFUL.getCode());
        response.setMessage(SuccessCode.DELETE_SUCCESSFUL.getMessage());
        return response;
    }
    
}
