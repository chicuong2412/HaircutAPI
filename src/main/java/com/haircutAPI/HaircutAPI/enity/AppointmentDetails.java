package com.haircutAPI.HaircutAPI.enity;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    @MapsId
    @JoinColumn(name = "idAppointment", referencedColumnName = "id")
    Appointment appointment;


    @ManyToMany(fetch = FetchType.EAGER)
    List<ServiceEntity> idService;
    @ManyToMany(fetch = FetchType.EAGER)
    List<ComboEntity> idCombo;

    double price;


}
