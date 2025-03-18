package com.haircutAPI.HaircutAPI.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.haircutAPI.HaircutAPI.enity.NotificationUser;

public interface NotificationUserRepository extends JpaRepository<NotificationUser, Integer> {

    public NotificationUser findByUserId(String userId);

    public boolean existsByUserId(String userId);
}
