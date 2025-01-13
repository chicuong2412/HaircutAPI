package com.haircutAPI.HaircutAPI.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkerInfoPublicResponse {
    String id;
    String nameWorker;
    String specialities;
    double Rate;
}
