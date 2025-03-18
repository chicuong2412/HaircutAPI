package com.haircutAPI.HaircutAPI.utils;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.SendResponse;
import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.dto.request.FirebaseNotification;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.dto.response.WorkerInfoPublicResponse;
import com.haircutAPI.HaircutAPI.dto.response.WorkerResponse;
import com.haircutAPI.HaircutAPI.enity.ComboEntity;
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.enity.FirebaseUserTokens;
import com.haircutAPI.HaircutAPI.enity.Location;
import com.haircutAPI.HaircutAPI.enity.Notification;
import com.haircutAPI.HaircutAPI.enity.ServiceEntity;
import com.haircutAPI.HaircutAPI.enity.TokenFirebase;
import com.haircutAPI.HaircutAPI.enity.User;
import com.haircutAPI.HaircutAPI.enity.Worker;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.mapper.WorkerMapper;
import com.haircutAPI.HaircutAPI.repositories.ComboRepository;
import com.haircutAPI.HaircutAPI.repositories.CustomerRepository;
import com.haircutAPI.HaircutAPI.repositories.FirebaseTokenUsersRepository;
import com.haircutAPI.HaircutAPI.repositories.LocationRepository;
import com.haircutAPI.HaircutAPI.repositories.ProductRepository;
import com.haircutAPI.HaircutAPI.repositories.ServiceRepository;
import com.haircutAPI.HaircutAPI.repositories.TokenRepository;
import com.haircutAPI.HaircutAPI.repositories.UserRepository;
import com.haircutAPI.HaircutAPI.repositories.WorkerRepository;
import com.haircutAPI.HaircutAPI.services.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicesUtils {

    private final CustomerRepository customerRepository;

    private final WorkerRepository workerRepository;

    private final LocationRepository locationRepository;

    private final ServiceRepository serviceRepository;

    private final ComboRepository comboRepository;

    private final ProductRepository productRepository;

    private final WorkerMapper workerMapper;

    private final NotificationService notificationService;

    private final UserRepository userRepository;

    private final FirebaseTokenUsersRepository firebaseTokenUsersRepository;

    private final TokenRepository tokenRepository;

    public boolean isCustomerIdExisted(String customerID) {
        return customerRepository.existsById(customerID);
    }

    public boolean isWorkerIdExisted(String workerID) {
        return workerRepository.existsById(workerID);
    }

    public boolean isProductIdExisted(String idProduct) {
        return productRepository.existsById(idProduct);
    }

    public boolean isLocationIdExisted(String idLocation) {
        return locationRepository.existsById(idLocation);
    }

    public Location findLocationByID(String idLocation) {
        return locationRepository.findById(idLocation).orElseThrow();
    }

    public boolean isIdServicesExisted(List<String> idServices) {
        for (String idService : idServices) {
            if (!serviceRepository.existsById(idService))
                return false;
        }
        return true;
    }

    public List<WorkerResponse> addAllLocationEntity(List<Worker> pre) {
        List<WorkerResponse> list = new ArrayList<>();
        // System.out.println("Entry");
        for (Worker preResponse : pre) {
            WorkerResponse rp = workerMapper.toWorkerResponse(preResponse);
            if (!preResponse.getUsername().equals("admin"))
                rp.setLocation(findLocationByID(preResponse.getIdLocation()));
            list.add(rp);
        }
        return list;
    }

    public List<WorkerInfoPublicResponse> addAllLocationEntityPublic(List<Worker> pre) {
        List<WorkerInfoPublicResponse> list = new ArrayList<>();
        for (Worker preResponse : pre) {
            System.out.println(preResponse.getUsername());
            if (!preResponse.getUsername().equals("admin")) {
                WorkerInfoPublicResponse rp = workerMapper.toWorkerInfoPublicResponse(preResponse);
                rp.setLocation(findLocationByID(preResponse.getIdLocation()));
                list.add(rp);
            }
        }
        return list;
    }

    public WorkerResponse addLocationEntity(Worker worker) { // map to location entity
        WorkerResponse rp = workerMapper.toWorkerResponse(worker);
        if (worker.getIdLocation() != null)
            rp.setLocation(findLocationByID(worker.getIdLocation()));

        return rp;
    }

    public boolean isIdCombosExisted(List<String> idCombos) {
        for (String idCombo : idCombos) {
            if (!comboRepository.existsById(idCombo))
                return false;
        }
        return true;
    }

    public boolean isIdProductsExisted(List<String> idProducts) {
        for (String idProduct : idProducts) {
            if (!productRepository.existsById(idProduct))
                return false;
        }
        return true;
    }

    public Customer findCustomerByUsername(String username) {
        return customerRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
    }

    public String findCustomerIDByUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.DATA_INPUT_INVALID));
        return customer.getId();
    }

    public HashSet<ServiceEntity> toServiceEntitiesSet(Set<String> listServices) {
        return new HashSet<>(serviceRepository.findAllById(listServices));
    }

    public HashSet<ComboEntity> toComboEnitiesSet(Set<String> listCombos) {
        return new HashSet<>(comboRepository.findAllById(listCombos));
    }

    public Customer getCustomerByID(String id) {
        return customerRepository.findById(id).orElseThrow();
    }

    public boolean checkAuthoritesHasRole(Collection<GrantedAuthority> auts, String role) {
        for (GrantedAuthority currRole : auts) {
            if (currRole.getAuthority().compareTo(role) == 0) {
                return true;
            }

        }

        return false;
    }

    public String getWorkerIdLocation(String username) {
        return workerRepository.findByUsername(username).getIdLocation();
    }

    public Worker findWorkerByUsername(String username) {
        return workerRepository.findByUsername(username);
    }

    public Worker findWorkerById(String username) {
        return workerRepository.findById(username).orElseThrow(() -> new AppException(ErrorCode.ID_WORKER_NOT_FOUND));
    }

    public String idGenerator(String prefix, String typeEntity) {
        if (typeEntity.equals("worker")) {
            return prefix + (workerRepository.count() + 1);
        } else if (typeEntity.equals("customer")) {
            return prefix + (customerRepository.count() + 1);
        } else if (typeEntity.equals("product")) {
            return prefix + (productRepository.count() + 1);
        } else if (typeEntity.equals("service")) {
            return prefix + (serviceRepository.count() + 1);
        } else if (typeEntity.equals("combo")) {
            return prefix + (comboRepository.count() + 1);
        } else {
            return prefix + (locationRepository.count() + 1);
        }
    }

    public void sendNotificationsToUsers(List<String> userIds, String header, String body) {
        for (String id : userIds) {
            sendNotificationsToUser(id, header, body);
        }
    }

    public void sendNotificationsToUser(String idUser, String header,
            String body) {
        notificationService.addNotificationToUser(header, body, idUser);
        try {
            FirebaseUserTokens firebaseUserTokens = firebaseTokenUsersRepository.findById(idUser)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_SIGN_FOR_NOTIFI));

            FirebaseNotification firebaseNotification = FirebaseNotification.builder()
                    .header(header)
                    .message(body)
                    .build();
            List<String> tokens = new ArrayList<>();
            firebaseUserTokens.getToken().forEach(t -> {
                tokens.add(t.getToken());
            });

            BatchResponse batchResponse = notificationService.sendMessages(firebaseNotification, tokens);
            checkListReponse(batchResponse, tokens, idUser);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public APIresponse<Set<Notification>> getMyNotifications(Authentication authentication) {
        return notificationService.getMyNotifications(authentication);
    }

    public void checkListReponse(BatchResponse batchResponse, List<String> tokens, String userID) {
        List<SendResponse> listResponses = batchResponse.getResponses();
        for (int i = 0; i < listResponses.size(); i++) {
            SendResponse sendResponse = listResponses.get(i);
            if (!sendResponse.isSuccessful()) {
                String unToken = tokens.get(i);
                deleteUnsuccessfulToken(userID, unToken);
            }
        }
    }

    public void deleteUnsuccessfulToken(String userID, String token) {
        FirebaseUserTokens firebaseUserTokens = firebaseTokenUsersRepository.findById(userID)
                .orElseThrow(() -> new AppException(ErrorCode.ID_NOT_FOUND));
        firebaseUserTokens.getToken().removeIf(t -> t.getToken().equals(token));
        firebaseTokenUsersRepository.save(firebaseUserTokens);
    }

    public void saveNoticationToUsers() {

    }

    public void saveUserTokenFirebase(String token, Authentication authentication) {
        User user = userRepository.findUserByUsername(authentication.getName());
        if (user == null)
            throw new AppException(ErrorCode.ID_NOT_FOUND);
        FirebaseUserTokens firebaseUserTokens = firebaseTokenUsersRepository.findById(user.getId())
                .orElse(new FirebaseUserTokens());
        firebaseUserTokens.setUserID(user.getId());

        TokenFirebase tokenFirebase = new TokenFirebase();
        tokenFirebase.setToken(token);
        tokenFirebase.setValid(true);
        tokenRepository.save(tokenFirebase);

        if (firebaseUserTokens.getToken() == null) {
            firebaseUserTokens.setToken(new HashSet<>());
        }

        if (!checkContain(firebaseUserTokens.getToken(), token)) {
            firebaseUserTokens.getToken().add(tokenFirebase);
        }

        firebaseTokenUsersRepository.save(firebaseUserTokens);
    }

    private boolean checkContain(Set<TokenFirebase> tokenFirebases, String token) {
        for (TokenFirebase i : tokenFirebases) {
            if (i.getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

}
