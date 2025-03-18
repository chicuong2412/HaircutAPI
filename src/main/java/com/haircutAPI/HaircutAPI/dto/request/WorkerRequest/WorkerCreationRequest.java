package com.haircutAPI.HaircutAPI.dto.request.WorkerRequest;

import com.haircutAPI.HaircutAPI.ENUM.RoleEmployee;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkerCreationRequest {

    @Size(min = 6, message = "USERNAME_LENGTH_INVALID")
    @NotNull(message = "NOTNULL")
    String username;
    @Size(min = 8, message = "PASSWORD_LENGTH_INVALID")
    @NotNull(message = "NOTNULL")
    String password;

    String nameWorker;

    String specialities;

    String file;

    double salary;

    double Rate;

    LocalDate DoB;

    @Email(message = "EMAIL_INVALID")
    String email;

    String address;

    String phoneNumber;

    @NotNull(message = "NOTNULL")
    String location;

    RoleEmployee idRole;
    
    // @NotNull(message = "NOTNULL")
    // LocalDate startDate;

}
