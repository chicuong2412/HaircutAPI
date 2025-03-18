package com.haircutAPI.HaircutAPI.dto.request;

import java.util.Map;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class FirebaseNotification {
    String header;
    String message;

    Map<String, String> data;
}
