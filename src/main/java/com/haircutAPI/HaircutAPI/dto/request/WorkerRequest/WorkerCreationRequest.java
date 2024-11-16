package com.haircutAPI.HaircutAPI.dto.request.WorkerRequest;

import com.haircutAPI.HaircutAPI.ENUM.RoleEmployee;

// import jakarta.persistence.EnumType;
// import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
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
    String username;
    @Size(min = 8, message = "PASSWORD_LENGTH_INVALID")
    String password;

    String nameWorker;
    String specialities;
    double salary;
    double Rate;
    String DoB;
    @Email(message = "EMAIL_INVALID")
    String email;
    String address;
    String phoneNumber;
    int idLocation;
    RoleEmployee idRole;
    String startDate;

}
