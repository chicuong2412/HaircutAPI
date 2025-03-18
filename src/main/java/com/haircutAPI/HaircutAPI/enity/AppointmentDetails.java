package com.haircutAPI.HaircutAPI.enity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentDetails {


    @Id
    @Column(name = "idAppointment")
    String id;


    @ManyToMany(fetch = FetchType.EAGER)
    Set<ServiceEntity> idService;
    @ManyToMany(fetch = FetchType.EAGER)
    Set<ComboEntity> idCombo;

    double price;


}
