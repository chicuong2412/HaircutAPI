package com.haircutAPI.HaircutAPI.services;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.dto.request.AuthenticationRequest;
import com.haircutAPI.HaircutAPI.dto.request.IntrospectRequest;
import com.haircutAPI.HaircutAPI.dto.response.IntrospectResponse;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.repositories.Authentication.CustomerAuthRepository;
import com.haircutAPI.HaircutAPI.repositories.Authentication.WorkerAuthRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class AuthenticationService {
    @Autowired
    WorkerAuthRepository workerAuthRepository;

    @Autowired
    CustomerAuthRepository customerAuthRepository;

    @Value("${jwt.SIGNED_KEY}")
    protected String SIGNED_KEY;

    public boolean authenticateWorker(AuthenticationRequest rq) {
        var worker = workerAuthRepository.findByUsername(rq.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(rq.getPassword(), worker.getPassword());
    }

    public boolean authenticateCustomer(AuthenticationRequest rq) {
        var customer = customerAuthRepository.findByUsername(rq.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(rq.getPassword(), customer.getPassword());
    }

    public IntrospectResponse introspect(IntrospectRequest rq) throws JOSEException, ParseException {
        

        String token = rq.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SIGNED_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean flag = signedJWT.verify(jwsVerifier);

        Date expityDate = signedJWT.getJWTClaimsSet().getExpirationTime();

        IntrospectResponse rp = new IntrospectResponse();
        
        rp.setValid(expityDate.after(new Date())&& flag);

        return rp;
    }

    public String generateJWT(String username) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
        .subject(username)
        .issuer("greatshang.com")
        .issueTime(new Date())
        .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
        .claim("JWT", "Jwt")
        .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNED_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            // TODO Auto-generated catch block
            
            throw new RuntimeException(e);
        }
    }

}
