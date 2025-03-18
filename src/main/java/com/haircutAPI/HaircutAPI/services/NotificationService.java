package com.haircutAPI.HaircutAPI.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.haircutAPI.HaircutAPI.ENUM.ErrorCode;
import com.haircutAPI.HaircutAPI.ENUM.SuccessCode;
import com.haircutAPI.HaircutAPI.dto.request.FirebaseNotification;
import com.haircutAPI.HaircutAPI.dto.response.APIresponse;
import com.haircutAPI.HaircutAPI.enity.NotificationUser;
import com.haircutAPI.HaircutAPI.exception.DefinedException.AppException;
import com.haircutAPI.HaircutAPI.repositories.NotificationRepository;
import com.haircutAPI.HaircutAPI.repositories.NotificationUserRepository;
import com.haircutAPI.HaircutAPI.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    private final NotificationRepository notificationRepository;

    private final NotificationUserRepository notificationUserRepository;

    private final UserRepository userRepository;

    public BatchResponse sendMessages(FirebaseNotification firebaseNotification, List<String> tokens) {

        Notification notification = Notification.builder()
                .setTitle(firebaseNotification.getHeader())
                .setBody(firebaseNotification.getMessage())
                .build();

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(notification)
                // .putAllData(firebaseNotification.getData())
                .build();

        try {
            return firebaseMessaging.sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public BatchResponse sendBackgroundMessages(FirebaseNotification firebaseNotification, Set<String> tokens) {

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                // .setNotification(notification)
                .putAllData(firebaseNotification.getData())
                .build();

        try {
            return firebaseMessaging.sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public void addNotificationToUser(String header, String message, String userId) {
        System.out.println("User ID: " + userId);
        com.haircutAPI.HaircutAPI.enity.Notification notification = new com.haircutAPI.HaircutAPI.enity.Notification();
        NotificationUser notificationUser = notificationUserRepository.findByUserId(userId);
        if (notificationUser == null) {
            notificationUser = new NotificationUser();
            notificationUser.setUserId(userId);
            notificationUser = notificationUserRepository.save(notificationUser);
        }

        notification.setHeader(header);
        notification.setMessage(message);
        notificationRepository.save(notification);
        notificationUser.setUserId(userId);
        notificationUser.getNotifications().add(notification);
        notificationUserRepository.save(notificationUser);
    }

    public APIresponse<Set<com.haircutAPI.HaircutAPI.enity.Notification>> getMyNotifications(Authentication authentication) {
        APIresponse<Set<com.haircutAPI.HaircutAPI.enity.Notification>> rp = new APIresponse<>(SuccessCode.GET_DATA_SUCCESSFUL.getCode());

        String userId = userRepository.findUserByUsername(authentication.getName()).getId();
        Set<com.haircutAPI.HaircutAPI.enity.Notification> notificationUserList;
        if (notificationUserRepository.existsByUserId(userId)) {
            var userNotifications = notificationUserRepository.findByUserId(userId);

            notificationUserList = userNotifications.getNotifications();
            
            userNotifications.setNotifications(new HashSet<>());
            notificationUserRepository.save(userNotifications);
            deleteNotiList(notificationUserList);
        } else {
            notificationUserList = new HashSet<>();
        }
        
        rp.setResult(notificationUserList);
        return rp;
    }

    public void deleteNotiList(Set<com.haircutAPI.HaircutAPI.enity.Notification> sets) {
        for (com.haircutAPI.HaircutAPI.enity.Notification notification : sets) {
            notificationRepository.deleteById(notification.getId());
        }
    }

}
