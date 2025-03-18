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
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.ENUM.UserType;
import com.haircutAPI.HaircutAPI.dto.request.AuthenticationRequest;
import com.haircutAPI.HaircutAPI.dto.request.ChangePasswordRequest;
import com.haircutAPI.HaircutAPI.dto.request.IntrospectRequest;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.AuthenticationResponse;
import com.haircutAPI.HaircutAPI.dto.response.IntrospectResponse;
import com.haircutAPI.HaircutAPI.enity.User;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.repositories.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    @Value("${jwt.SIGNED_KEY}")
    protected String SIGNED_KEY;

    public APIresponse<AuthenticationResponse> authenticateWorker(AuthenticationRequest rq, String password) {
        var worker = workerAuthRepository.findByUsername(rq.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean loginFlag = passwordEncoder.matches(rq.getPassword(), password);
        APIresponse<AuthenticationResponse> rp = new APIresponse<>(1000);

        rp.setResult(new AuthenticationResponse());
        rp.getResult().setAuthenticated(loginFlag);

        String role = buildScope(worker.getId(), worker.getIdRole().name());
        if (loginFlag)
            rp.getResult().setToken(generateJWT(rq.getUsername(), role, worker.getId()));

        rp.getResult().setRole(role);
        rp.getResult().setId(worker.getId());
        rp.getResult().setIdLocation(worker.getIdLocation());
        return rp;
    }

    public APIresponse<AuthenticationResponse> authenticateCustomer(AuthenticationRequest rq, String password) {
        var customer = customerAuthRepository.findByUsername(rq.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean loginFlag = passwordEncoder.matches(rq.getPassword(), password);
        APIresponse<AuthenticationResponse> rp = new APIresponse<>(1000);

        rp.setResult(new AuthenticationResponse());
        rp.getResult().setAuthenticated(loginFlag);

        String role = buildScope(customer.getId(), customer.getTypeCustomer().name());
        if (loginFlag)
            rp.getResult().setToken(generateJWT(rq.getUsername(), role, customer.getId()));

        rp.getResult().setRole(role);
        rp.getResult().setId(customer.getId());
        return rp;
    }

    public APIresponse<AuthenticationResponse> login(AuthenticationRequest rq) {
        APIresponse<AuthenticationResponse> rp = new APIresponse<>(0);
     
        if (userRepository.existsByUsername(rq.getUsername())) {
           
            User user = userRepository.findUserByUsername(rq.getUsername());
            
            if (user.getRoles().contains(UserType.WORKER.getName())) {
                return authenticateWorker(rq, user.getPassword());
            } else {
             
                return authenticateCustomer(rq, user.getPassword());
            }
        }
        rp.setCode(ErrorCode.LOGIN_FAILED.getCode());
        rp.setMessage(ErrorCode.LOGIN_FAILED.getMessage());
        return rp;
    }

    public APIresponse<String> changePassword(ChangePasswordRequest rq) {
        APIresponse<String> rp = new APIresponse<>(SuccessCode.CHANGE_PASSWORD_SUCCESSFUL.getCode());
        User user = userRepository.findUserByUsername(rq.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        System.out.println(rq.getOldPassword());
        System.out.println(user.getPassword());
        System.out.println(passwordEncoder.matches(rq.getOldPassword(), user.getPassword()));
        boolean loginFlag = passwordEncoder.matches(rq.getOldPassword(), user.getPassword());

        if (loginFlag) {
            user.setPassword(passwordEncoder.encode(rq.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new AppException(ErrorCode.CHANGE_PASSSWORD_FAILED);
        }
        rp.setMessage(SuccessCode.CHANGE_PASSWORD_SUCCESSFUL.getMessage());
        return rp;
    }

    public IntrospectResponse introspect(IntrospectRequest rq) throws JOSEException, ParseException {

        String token = rq.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SIGNED_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean flag = signedJWT.verify(jwsVerifier);

        Date expityDate = signedJWT.getJWTClaimsSet().getExpirationTime();

        IntrospectResponse rp = new IntrospectResponse();

        rp.setValid(expityDate.after(new Date()) && flag);

        return rp;
    }

    public String generateJWT(String username, String role, String id) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("greatshang.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(10000, ChronoUnit.HOURS).toEpochMilli()))
                .claim("id", id)
                .claim("scope", role)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNED_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {

            throw new RuntimeException(e);
        }
    }

    private String buildScope(String userID, String role) {
        StringBuilder out = new StringBuilder();
        User user = userRepository.findById(userID).orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));

        user.getRoles().forEach(t -> {

            out.append(t).append(" ");
        });
        out.append(role);
        return out.toString();
    }

}
