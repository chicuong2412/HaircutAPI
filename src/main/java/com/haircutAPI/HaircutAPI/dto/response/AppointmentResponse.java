package com.haircutAPI.HaircutAPI.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

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

    Set<ServiceEntity> idService;
    Set<ComboEntity> idCombo;

    double price;
}
