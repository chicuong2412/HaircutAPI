package com.haircutAPI.HaircutAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.haircutAPI.HaircutAPI.enity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
}
