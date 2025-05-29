package com.haircutAPI.HaircutAPI.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.checkerframework.checker.units.qual.s;

import com.haircutAPI.HaircutAPI.ENUM.AppointmentStatus;
import com.haircutAPI.HaircutAPI.enity.ComboEntity;
import com.haircutAPI.HaircutAPI.enity.Customer;
import com.haircutAPI.HaircutAPI.enity.ServiceEntity;
import com.haircutAPI.HaircutAPI.enity.Worker;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    String id;
    Customer customer;
    Worker worker;
    AppointmentStatus status;

    String idLocation;

    LocalDateTime dateTime;

    List<ServiceEntity> idService;
    List<ComboEntity> idCombo;

    double price;
    boolean deleted;

    public AppointmentResponse(String id, Customer customer, Worker worker, String idLocation,
            AppointmentStatus status, LocalDateTime dateTime, double price,
            boolean deleted) {
        this.id = id;
        this.customer = customer;
        this.worker = worker;
        this.idLocation = idLocation;
        this.status = status;
        this.price = price;
        this.deleted = deleted;
        this.dateTime = dateTime;
    }

}
