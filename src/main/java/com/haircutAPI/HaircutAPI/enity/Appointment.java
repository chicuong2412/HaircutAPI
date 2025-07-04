package com.haircutAPI.HaircutAPI.enity;

import java.time.LocalDateTime;

import com.haircutAPI.HaircutAPI.ENUM.AppointmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idAppointment")
    String id;

    String idCustomer;
    String idWorker;

    @ManyToOne
    @JoinColumn(name = "idCustomer", referencedColumnName = "id", insertable = false, updatable = false)
    Customer customer;

    @ManyToOne
    @JoinColumn(name = "idWorker", referencedColumnName = "id", insertable = false, updatable = false)
    Worker worker;

    @Enumerated(EnumType.ORDINAL)
    AppointmentStatus status;

    String idLocation;

    LocalDateTime dateTime;

    @Column(columnDefinition = "boolean default false")
    boolean isDeleted;
    

}
