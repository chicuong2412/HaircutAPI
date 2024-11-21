package com.haircutAPI.HaircutAPI.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIresponse<T> {


    private int code;
    private String message;


    private T result;


    public APIresponse(int code) {
        this.code = code;
    }

    
}
