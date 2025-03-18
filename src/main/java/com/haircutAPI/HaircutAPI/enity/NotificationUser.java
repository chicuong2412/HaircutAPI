package com.haircutAPI.HaircutAPI.enity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class NotificationUser {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String userId;


    @OneToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    Set<Notification> notifications = new HashSet<>();
    


}
