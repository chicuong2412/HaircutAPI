package com.haircutAPI.HaircutAPI.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haircutAPI.HaircutAPI.dto.request.AuthenticationRequest;
import com.haircutAPI.HaircutAPI.dto.request.IntrospectRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.AuthenticationResponse;
import com.haircutAPI.HaircutAPI.dto.response.IntrospectResponse;
import com.haircutAPI.HaircutAPI.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/workers/login")
    APIresponse<AuthenticationResponse> loginForWorker(@RequestBody AuthenticationRequest rq) {
        boolean loginFlag = authenticationService.authenticateWorker(rq);
        APIresponse<AuthenticationResponse> rp = new APIresponse<>(1000);

        rp.setResult(new AuthenticationResponse());
        rp.getResult().setAuthenticated(loginFlag);

        if (loginFlag) rp.getResult().setToken(authenticationService.generateJWT(rq.getUsername())); 
        
        return rp;
    }

    @PostMapping("/customers/login")
    APIresponse<AuthenticationResponse> loginForCustomer(@RequestBody AuthenticationRequest rq) {
        boolean loginFlag = authenticationService.authenticateCustomer(rq);
        APIresponse<AuthenticationResponse> rp = new APIresponse<>(1000);

        rp.setResult(new AuthenticationResponse());
        rp.getResult().setAuthenticated(loginFlag);

        if (loginFlag) rp.getResult().setToken(authenticationService.generateJWT(rq.getUsername())); 
        return rp;
    }

    @PostMapping("/introspect")
    APIresponse<IntrospectResponse> postMethodName(@RequestBody IntrospectRequest rq) {
        var rp = new APIresponse<IntrospectResponse>(202);
        try {
            rp.setResult(authenticationService.introspect(rq));
        } catch (JOSEException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rp;
    }
    

    

}
