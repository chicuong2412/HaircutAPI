package com.haircutAPI.HaircutAPI.dto.request.AppointmetRequest;

import java.time.LocalDateTime;
import java.util.List;

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
public class AppointmentCreationRequest {

    @NotNull(message = "NOTNULL")
    String idCustomer;

    @NotNull(message = "NOTNULL")
    String idWorker;

    @NotNull(message = "NOTNULL")
    AppointmentStatus status;

    @NotNull(message = "NOTNULL")
    String idLocation;

    @NotNull(message = "NOTNULL")
    @DateTimeFormat(pattern = "dd-MM-yyyyThh:mm")
    LocalDateTime dateTime;

    @NotNull(message = "NOTNULL")
    List<String> idService;
    @NotNull(message = "NOTNULL")
    List<String> idCombo;

    @NotNull(message = "NOTNULL")
    double price;
}
