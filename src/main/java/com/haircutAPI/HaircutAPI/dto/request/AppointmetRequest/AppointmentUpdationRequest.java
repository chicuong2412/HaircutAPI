package com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.haircutAPI.HaircutAPI.ENUM.AppointmentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentUpdationRequest {

    @NotNull(message = "NOTNULL")
    String id;

    String idCustomer;

    String idWorker;

    AppointmentStatus status;

    String idLocation;

    @DateTimeFormat(pattern = "dd-MM-yyyyThh:mm")
    LocalDateTime dateTime;

    Set<String> idService;

    @NotNull(message = "NOTNULL")
    Set<String> idCombo;

}