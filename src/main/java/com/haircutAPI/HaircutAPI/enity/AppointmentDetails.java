package com.haircutAPI.HaircutAPI.enity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    List<String> idService;
    List<String> idCombo;

    double price;


}
