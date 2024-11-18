package com.haircutAPI.HaircutAPI.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.haircutAPI.HaircutAPI.ENUM.AppointmentStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentResponse {

    String id;
    String idCustomer;
    String idWorker;
    AppointmentStatus status;

    String idLocation;

    LocalDateTime dateTime;

    List<String> idService;
    List<String> idCombo;
}
