package com.haircutAPI.HaircutAPI.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.TokenRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.enity.Notification;
import com.haircutAPI.HaircutAPI.utils.ServicesUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class FirebaseController {

    private final ServicesUtils servicesUtils;

    @PostMapping
    public APIresponse<String> receiveToken(@RequestBody TokenRequest token) {
        APIresponse<String> rp = new APIresponse<>(SuccessCode.CREATE_SUCCESSFUL.getCode());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        servicesUtils.saveUserTokenFirebase(token.getToken(), authentication);

        return rp;
    }

    @GetMapping
    public APIresponse<Set<Notification>> getMyNotifications() {
        var rp = servicesUtils.getMyNotifications(SecurityContextHolder.getContext().getAuthentication());
        return rp;
    }
}
