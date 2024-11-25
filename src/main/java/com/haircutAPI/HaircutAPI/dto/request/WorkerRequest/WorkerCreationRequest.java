package com.haircutAPI.HaircutAPI.dto.request.WorkerRequest;

import com.haircutAPI.HaircutAPI.ENUM.RoleEmployee;

// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
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

    @NotNull(message = "NOTNULL")
    String nameWorker;

    @NotNull(message = "NOTNULL")
    String specialities;

    @NotNull(message = "NOTNULL")
    double salary;

    @NotNull(message = "NOTNULL")
    double Rate;

    @NotNull(message = "NOTNULL")
    String DoB;

    @Email(message = "EMAIL_INVALID")
    @NotNull(message = "NOTNULL")
    String email;

    @NotNull(message = "NOTNULL")
    String address;

    @NotNull(message = "NOTNULL")
    String phoneNumber;

    @NotNull(message = "NOTNULL")
    String idLocation;

    @NotNull(message = "NOTNULL")
    RoleEmployee idRole;
    
    @NotNull(message = "NOTNULL")
    String startDate;

}
